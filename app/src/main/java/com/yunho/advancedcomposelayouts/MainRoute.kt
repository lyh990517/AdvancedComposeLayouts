package com.yunho.advancedcomposelayouts

import kotlinx.serialization.Serializable

@Serializable
sealed interface MainRoute

@Serializable
data object Animated3DPages : MainRoute

@Serializable
data object AnimatedGradient : MainRoute

@Serializable
data object MetallicShaderCard : MainRoute

@Serializable
data object Root : MainRoute

