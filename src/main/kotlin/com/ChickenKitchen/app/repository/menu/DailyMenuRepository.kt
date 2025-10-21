package com.ChickenKitchen.app.repository.menu

import com.ChickenKitchen.app.model.entity.menu.DailyMenu
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
interface DailyMenuRepository : JpaRepository<DailyMenu, Long> {

    fun existsByMenuDate(menuDate: Timestamp): Boolean

    @Query(
        """
        select dm from DailyMenu dm
        join dm.stores s
        where s.id = :storeId and dm.menuDate between :start and :end
        """
    )
    fun findByStoreAndDateRange(
        @Param("storeId") storeId: Long,
        @Param("start") start: Timestamp,
        @Param("end") end: Timestamp
    ): DailyMenu?
}
