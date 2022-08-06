package com.example.newcalendar.di

import com.example.newcalendar.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.newcalendar.viewmodel.*

val memoViewModelModule = module {
    viewModel {
        MemoViewModel(get())
    }
}

val scheduleViewModelModule = module {
    viewModel {
        ScheduleViewModel(get())
    }
}

val eventViewModelModule = module {
    viewModel {
        EventViewModel(get())
    }
}

val alarmViewModelModule = module {
    viewModel {
        AlarmViewModel(get())
    }
}