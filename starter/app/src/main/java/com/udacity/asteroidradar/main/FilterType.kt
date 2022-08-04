package com.udacity.asteroidradar.main

import androidx.annotation.IdRes
import com.udacity.asteroidradar.R

enum class FilterType(@IdRes val menuItemId: Int) {
    WEEK(R.id.show_all_menu),
    TODAY(R.id.show_rent_menu),
    SAVED(R.id.show_buy_menu)
}