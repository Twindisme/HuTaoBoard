// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.updates

import android.content.ClipData
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import helium314.keyboard.latin.BuildConfig
import helium314.keyboard.latin.R
import helium314.keyboard.latin.utils.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private enum class UpdateStage {
    READY,
    DOWNLOADING,
    ALLOW_INSTALLS,
    OPENING_INSTALLER,
    ERROR,
}

class HuTaoUpdateActivity : ComponentActivity() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var stage by mutableStateOf(UpdateStage.READY)
    private var errorMessage by mutableStateOf<String?>(null)
    private var pendingInstall: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Theme(dark = true) {
                Surface(color = HuTaoBackground) {
                    UpdateScreen(
                        update = HuTaoUpdater.getAvailableUpdate(this),
                        stage = stage,
                        errorMessage = errorMessage,
                        onBack = ::finish,
                        onDownload = ::downloadAndInstall,
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val file = pendingInstall ?: return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || packageManager.canRequestPackageInstalls()) {
            pendingInstall = null
            launchInstaller(file)
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private fun downloadAndInstall() {
        val update = HuTaoUpdater.getAvailableUpdate(this) ?: return
        errorMessage = null
        stage = UpdateStage.DOWNLOADING
        scope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    HuTaoUpdater.downloadAndVerify(this@HuTaoUpdateActivity, update)
                }
            }.onSuccess { requestInstall(it) }
                .onFailure {
                    errorMessage = it.message ?: it.javaClass.simpleName
                    stage = UpdateStage.ERROR
                }
        }
    }

    private fun requestInstall(file: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !packageManager.canRequestPackageInstalls()) {
            pendingInstall = file
            stage = UpdateStage.ALLOW_INSTALLS
            startActivity(
                Intent(
                    Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                    "package:$packageName".toUri(),
                ),
            )
            return
        }
        launchInstaller(file)
    }

    private fun launchInstaller(file: File) {
        stage = UpdateStage.OPENING_INSTALLER
        runCatching {
            val uri = FileProvider.getUriForFile(this, "$packageName.updates", file)
            val intent = Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, "application/vnd.android.package-archive")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.clipData = ClipData.newRawUri("Hu Tao Board update", uri)
            startActivity(intent)
        }.onFailure {
            errorMessage = it.message ?: it.javaClass.simpleName
            stage = UpdateStage.ERROR
        }
    }
}

private val HuTaoBackground = Color(0xFF24181B)
private val HuTaoPanel = Color(0xE6382226)
private val HuTaoRed = Color(0xFF713038)
private val HuTaoGold = Color(0xFFE6C58D)
private val HuTaoIvory = Color(0xFFFFF1D9)

@Composable
private fun UpdateScreen(
    update: HuTaoUpdateInfo?,
    stage: UpdateStage,
    errorMessage: String?,
    onBack: () -> Unit,
    onDownload: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HuTaoBackground),
    ) {
        Image(
            painter = painterResource(R.drawable.hu_tao_keyboard_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.24f),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextButton(onClick = onBack) {
                    Text("‹", color = HuTaoGold, fontSize = 36.sp)
                }
                Text(
                    text = stringResource(R.string.hu_tao_update_screen_title),
                    color = HuTaoIvory,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(40.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, HuTaoGold, RoundedCornerShape(18.dp))
                    .background(HuTaoPanel, RoundedCornerShape(18.dp))
                    .padding(horizontal = 24.dp, vertical = 32.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.hu_tao_toolbar_backpack),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                )
                Spacer(Modifier.height(16.dp))
                if (update == null) {
                    Text(
                        text = stringResource(R.string.hu_tao_update_up_to_date),
                        color = HuTaoIvory,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.hu_tao_update_ready),
                        color = HuTaoGold,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.hu_tao_update_version, update.versionName),
                        color = HuTaoIvory,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(R.string.hu_tao_update_current_version, BuildConfig.VERSION_NAME),
                        color = HuTaoGold.copy(alpha = 0.75f),
                        fontSize = 14.sp,
                    )
                    Spacer(Modifier.height(24.dp))
                    when (stage) {
                        UpdateStage.READY, UpdateStage.ERROR -> {
                            if (stage == UpdateStage.ERROR && errorMessage != null) {
                                Text(
                                    text = stringResource(R.string.hu_tao_update_failed, errorMessage),
                                    color = Color(0xFFFFAAA7),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 14.dp),
                                )
                            }
                            Button(
                                onClick = onDownload,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HuTaoRed,
                                    contentColor = HuTaoIvory,
                                ),
                            ) {
                                Text(
                                    text = stringResource(
                                        if (stage == UpdateStage.ERROR) R.string.hu_tao_update_retry
                                        else R.string.hu_tao_update_download,
                                    ),
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                        UpdateStage.DOWNLOADING -> UpdateProgress(R.string.hu_tao_update_downloading)
                        UpdateStage.ALLOW_INSTALLS -> UpdateProgress(R.string.hu_tao_update_allow_installs)
                        UpdateStage.OPENING_INSTALLER -> UpdateProgress(R.string.hu_tao_update_opening_installer)
                    }
                }
            }
        }
    }
}

@Composable
private fun UpdateProgress(message: Int) {
    CircularProgressIndicator(color = HuTaoGold)
    Spacer(Modifier.height(16.dp))
    Text(
        text = stringResource(message),
        color = HuTaoIvory,
        textAlign = TextAlign.Center,
    )
}
