package org.example.project.platform

import org.koin.core.context.GlobalContext

actual fun getPlatform(): Platform {
    return GlobalContext.get().get<Platform>()
}
