package com.fires.domain.portfolio.adapter

import com.fires.common.exception.ErrorCode
import com.fires.common.exception.ServiceException
import com.fires.domain.portfolio.adapter.entity.PortfolioAssetEntity
import com.fires.domain.portfolio.adapter.entity.PortfolioEntity
import com.fires.domain.portfolio.adapter.repository.PortfolioAssetRepository
import com.fires.domain.portfolio.adapter.repository.PortfolioRepository
import com.fires.domain.portfolio.application.port.out.PortfolioAssetAddCommand
import com.fires.domain.portfolio.application.port.out.PortfolioAssetUpdateCommand
import com.fires.domain.portfolio.application.port.out.PortfolioPort
import com.fires.domain.portfolio.domain.entity.Portfolio
import com.fires.domain.portfolio.domain.entity.SimplePortfolioAsset
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class PortfolioAdapter(
    private val portfolioRepository: PortfolioRepository,
    private val portfolioAssetRepository: PortfolioAssetRepository
) : PortfolioPort {

    /**
     * 자산 pirority는 0부터 시작한다.
     */
    override fun createPortfolio(userId: Long): Portfolio {
        // 기존 포트폴리오가 있을 경우 마지막 포트폴리오의 priority를 가져오고, 없다면 -1로 초기화
        val lastPriority = portfolioRepository.findTop1ByUserIdOrderByPriorityAsc(userId)
            ?.priority
            ?: -1
        val portfolioEntity = PortfolioEntity(
            userId = userId,
            priority = lastPriority + 1
        )
        return portfolioRepository.save(portfolioEntity).toPortfolio()
    }

    @Transactional(readOnly = true)
    override fun countPortfolioOfUser(userId: Long): Long =
        portfolioRepository.countByUserId(userId)

    override fun addAssets(portfolioId: Long, assets: List<PortfolioAssetAddCommand>): List<PortfolioAssetEntity> {
        val portfolioEntity = portfolioRepository.findByIdOrNull(portfolioId) ?: return emptyList()
        val portfolioAssetEntityList = portfolioAssetRepository.saveAll(assets.map { it.toPortfolioEntity() })
        portfolioEntity.addPortfolioAssetEntityList(portfolioAssetEntityList)
        return portfolioAssetEntityList
    }

    override fun updateAssets(
        portfolioId: Long,
        assets: List<PortfolioAssetUpdateCommand>
    ): List<PortfolioAssetEntity> {
        val portfolioAssetEntityMap: Map<Long, PortfolioAssetEntity>
            = portfolioAssetRepository.findAllById(assets.map { it.portfolioAssetId }).associateBy { it.id!! }
        return assets.mapNotNull {
            val entity = portfolioAssetEntityMap[it.portfolioAssetId]
            entity?.update(
                count = it.count,
                purchasePrice = it.purchasePrice,
                currencyType = it.currencyType,
                order = it.order
            )
        }
    }

    override fun deleteAsset(portfolioAssetId: Long) {
        val portfolioAssetEntity = portfolioAssetRepository.findByIdOrNull(portfolioAssetId) ?: throw ServiceException(ErrorCode.PORTFOLIO_ASSET_NOT_FOUND)
        portfolioAssetEntity.delete() // 양방향 연관관계 시 양쪽 모두 삭제 후 delete 쿼리 실행됨
        portfolioAssetRepository.delete(portfolioAssetEntity)
    }

    override fun deletePortfolio(portfolioId: Long) {
        val portfolioEntity = portfolioRepository.findByIdOrNull(portfolioId) ?: throw ServiceException(ErrorCode.PORTFOLIO_NOT_FOUND)
        portfolioRepository.delete(portfolioEntity)
    }

    override fun isPortfolioExist(portfolioId: Long): Boolean {
        return portfolioRepository.existsById(portfolioId)
    }

    override fun isPortfolioAssetExist(portfolioAssetId: Long): Boolean {
        return portfolioAssetRepository.existsById(portfolioAssetId)
    }

    @Transactional(readOnly = true)
    override fun getPortfolioAsset(portfolioAssetId: Long): PortfolioAssetEntity {
        return portfolioAssetRepository.findByIdOrNull(portfolioAssetId) ?: throw ServiceException(ErrorCode.PORTFOLIO_ASSET_NOT_FOUND)
    }

    @Transactional(readOnly = true)
    override fun getPortfolioAssetByPortfolioId(portfolioId: Long): List<SimplePortfolioAsset>{
        val portfolioEntity = portfolioRepository.findByIdOrNull(portfolioId) ?: throw ServiceException(ErrorCode.PORTFOLIO_NOT_FOUND)
        return portfolioAssetRepository.findByPortfolioEntity(portfolioEntity).map { it.toSimplePortfolioAsset() }
    }

    @Transactional(readOnly = true)
    override fun getPortfolio(portfolioId: Long): Portfolio =
        portfolioRepository.findByIdOrNull(portfolioId)
            ?.toPortfolio()
            ?: throw ServiceException(ErrorCode.PORTFOLIO_NOT_FOUND)

    @Transactional(readOnly = true)
    override fun getDefaultPortfolio(userId: Long): Portfolio {
        return portfolioRepository.findTop1ByUserIdOrderByPriorityAsc(userId)?.toPortfolio() ?: throw ServiceException(ErrorCode.PORTFOLIO_NOT_FOUND)
    }
}
