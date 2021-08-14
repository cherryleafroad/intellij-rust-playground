package com.cherryleafroad.rust.playground.utils

import com.cherryleafroad.rust.playground.config.SettingsConfigurable
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import org.rust.cargo.project.settings.toolchain
import org.rust.cargo.runconfig.hasCargoProject
import org.rust.cargo.toolchain.tools.cargo

object Helpers {
    fun checkAndNotifyCargoExpandInstalled(project: Project): Boolean {
        val installed = project.toolchain?.hasCargoExecutable("cargo-expand") ?: false

        if (project.toolchain != null && !installed) {
            if (project.hasCargoProject) {
                val notification = NotificationGroupManager.getInstance().getNotificationGroup("Rust Playground")
                    .createNotification(
                        "Rust Playground",
                        "Cargo-expand is required to use the expand feature",
                        NotificationType.INFORMATION
                    )

                val install = NotificationAction.createSimple("Install") {
                    val toolchain = project.toolchain!!
                    toolchain.cargo().installBinaryCrate(project, "cargo-expand")
                    notification.hideBalloon()
                }
                val dismiss = NotificationAction.createSimple("Dismiss") {
                    notification.hideBalloon()
                }
                notification.addAction(install)
                notification.addAction(dismiss)
                notification.notify(project)
            } else {
                val notification = NotificationGroupManager.getInstance().getNotificationGroup("Rust Playground")
                    .createNotification(
                        "Rust Playground",
                        "Cargo-expand is required to use the expand feature. Auto-install won't work on non-Cargo projects. Please manually install or use a Cargo project",
                        NotificationType.INFORMATION
                    )

                val dismiss = NotificationAction.createSimple("Dismiss") {
                    notification.hideBalloon()
                }
                notification.addAction(dismiss)

                notification.notify(project)
            }
        }

        return installed
    }

    @Suppress("DialogTitleCapitalization")
    fun checkAndNotifyCargoPlayInstallation(project: Project): Boolean {
        val installed = project.toolchain?.hasCargoExecutable("cargo-play") ?: false

        if (project.toolchain != null && !installed) {
            if (project.hasCargoProject) {
                val notification = NotificationGroupManager.getInstance().getNotificationGroup("Rust Playground")
                    .createNotification(
                        "Rust Playground",
                        "Playground requires cargo-play binary crate",
                        NotificationType.INFORMATION
                    )

                val install = NotificationAction.createSimple("Install") {
                    val toolchain = project.toolchain!!
                    toolchain.cargo().installBinaryCrate(project, "cargo-play")
                    notification.hideBalloon()
                }
                val settings = NotificationAction.createSimple("Settings") {
                    ShowSettingsUtil.getInstance().showSettingsDialog(project, SettingsConfigurable::class.java)
                }
                val dismiss = NotificationAction.createSimple("Dismiss") {
                    notification.hideBalloon()
                }
                notification.addAction(install)
                notification.addAction(settings)
                notification.addAction(dismiss)
                notification.notify(project)
            } else {
                val notification = NotificationGroupManager.getInstance().getNotificationGroup("Rust Playground")
                    .createNotification(
                        "Rust Playground",
                        "Playground requires cargo-play binary crate. Auto-install won't work on non-Cargo projects. Please manually install or use a Cargo project",
                        NotificationType.INFORMATION
                    )

                val settings = NotificationAction.createSimple("Settings") {
                    ShowSettingsUtil.getInstance().showSettingsDialog(project, SettingsConfigurable::class.java)
                }
                val dismiss = NotificationAction.createSimple("Dismiss") {
                    notification.hideBalloon()
                }
                notification.addAction(settings)
                notification.addAction(dismiss)
                notification.notify(project)
            }
        }

        return installed
    }
}
