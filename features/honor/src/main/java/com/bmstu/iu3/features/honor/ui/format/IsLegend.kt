package com.bmstu.iu3.features.honor.ui.format

import com.bmstu.iu3.core.model.Honor
import com.bmstu.iu3.core.model.HonorKind

internal fun Honor.isLegend() = kind == HonorKind.Legend
