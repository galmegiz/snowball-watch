package com.fires.domain.portfolio.domain

import com.fires.common.exception.ErrorCode
import com.fires.common.exception.ServiceException
import com.fires.common.logging.Log
import com.fires.domain.portfolio.application.port.`in`.PortfolioAssetAddRequest
import com.fires.domain.portfolio.application.port.`in`.PortfolioAssetUpdateRequest
import com.fires.domain.portfolio.application.port.`in`.PortfolioManageUseCase
import com.fires.domain.portfolio.application.port.out.PortfolioAssetAddCommand
import com.fires.domain.portfolio.application.port.out.PortfolioAssetUpdateCommand
import com.fires.domain.portfolio.application.port.out.PortfolioPort
import com.fires.domain.portfolio.costant.PortfolioConstant
import com.fires.domain.portfolio.domain.dto.PortfolioAssetDeleteRequest
import com.fires.domain.portfolio.domain.dto.PortfolioCreateInfo
import com.fires.domain.portfolio.domain.entity.Portfolio
import com.fires.domain.portfolio.domain.entity.SimplePortfolioAsset
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class PortfolioManageService(
    private val portfolioPort: PortfolioPort,
) : PortfolioManageUseCase, Log {

    override fun createPortfolio(userId: Long): PortfolioCreateInfo {
        if (portfolioPort.countPortfolioOfUser(userId) >= PortfolioConstant.MAX_PORTFOLIO_COUNT) {
            throw ServiceException(ErrorCode.PORTFOLIO_COUNT_OVER)
        }
        // ToDo 현재는 포트폴리오를 1개만 생성가능하므로 prioriry를 정적으로 설정, 향후 마지막 pirority를 얻어오는 로직 추가 필요
        return PortfolioCreateInfo.from(portfolioPort.createPortfolio(userId))
    }

    override fun deletePortfolio(portfolioId: Long, userId: Long): Boolean {
        val portfolio = portfolioPort.getPortfolio(portfolioId)
                                        .withValidUserCheck(userId)

        portfolioPort.deletePortfolio(portfolio.id)
        return !portfolioPort.isPortfolioExist(portfolioId)
    }

    override fun addAssets(request: PortfolioAssetAddRequest, userId: Long): List<SimplePortfolioAsset> {
        val portfolio = portfolioPort.getPortfolio(request.portfolioId)
                                        .withValidUserCheck(userId)
        var lastAssetPriority = getLastPriority(portfolio)
        val addCommands: MutableList<PortfolioAssetAddCommand> = mutableListOf()

        for (asset in request.assets) {
            lastAssetPriority += 1
            val command = asset.toPortfolioAssetAddCommand(lastAssetPriority)
            addCommands.add(command)
        }

        return portfolioPort.addAssets(portfolio.id, addCommands)
            .map { it.toSimplePortfolioAsset() }
    }

    private fun getLastPriority(portfolio: Portfolio): Int{
        if (portfolio.assetList.isEmpty()) {
            return -1
        }
        val lastPriority = portfolio.assetList.maxBy { it.priority }.priority
        // 신규 추가되는 자산은 무조건 마지막 자산보다 우선순위가 낮아야됨(값이 작아야 됨)
        return if (lastPriority > 0) lastPriority else portfolio.assetList.size - 1
    }


    override fun updateAssets(request: PortfolioAssetUpdateRequest, userId: Long): List<SimplePortfolioAsset> {
        val portfolio = portfolioPort.getPortfolio(request.portfolioId)
                                            .withValidUserCheck(userId)

        val updateCommands: List<PortfolioAssetUpdateCommand> =
            request.assets.map { it.toPortfolioAssetUpdateCommand() }

        return portfolioPort.updateAssets(portfolio.id, updateCommands)
            .map { it.toSimplePortfolioAsset() }
    }


    override fun deleteAsset(request: PortfolioAssetDeleteRequest, userId: Long): Boolean {
        val portfolio = portfolioPort.getPortfolio(request.portfolioId)
                                            .withValidUserCheck(userId)

        portfolioPort.deleteAsset(request.portfolioAssetId)
        return !portfolioPort.isPortfolioAssetExist(request.portfolioAssetId)
    }
}