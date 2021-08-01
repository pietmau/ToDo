package com.pppp.uielements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.datetimepicker

interface Calendar {

    companion object {
        @Composable
        fun Content(
            due: Long? = null,
            onTimeDataPicked: (Long?) -> Unit = {}
        ) {
            val dialog = remember { MaterialDialog() }
            dialog.build {
                datetimepicker(
                    initialDateTime = due.toLocalDateTime()
                ) {
                    onTimeDataPicked(it.toMillis())
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(all = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .clickable {
                                dialog.show()
                            },
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = "",
                    )
                    Text(
                        text = due.toDueDateText() ?: "Due",
                        style = MaterialTheme.typography.caption
                    )
                    due?.let {
                        Image(
                            modifier = Modifier
                                .clickable {
                                    onTimeDataPicked(null)
                                },
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "",
                        )
                    }
                }
            }
        }

    }
}