package com.example.domain.usecases.animation_duration

import com.example.domain.models.AnimationDuration

class AnimationDurationUseCase {

    fun getAnimationDuration(): AnimationDuration {
        return AnimationDuration(scaleDuration = 10_000, rotationDuration = 13_500)
    }
}

