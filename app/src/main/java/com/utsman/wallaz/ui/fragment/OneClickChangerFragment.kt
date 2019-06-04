/*
 * Copyright 2019 Muhammad Utsman. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utsman.wallaz.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.utsman.wallaz.MainActivity
import com.utsman.wallaz.R
import kotlinx.android.synthetic.main.one_click_changer_fragment.*

class OneClickChangerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.one_click_changer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        launch_shortcut.setOnClickListener {
            val intent = Intent("com.android.launcher.action.INSTALL_SHORTCUT")
            intent.putExtra("duplicate", false)
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "tesss")

            val icon = Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_changer) as Parcelable
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon)
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, Intent(context, MainActivity::class.java))
            context?.sendBroadcast(intent)
        }

// public void createShortCut(){
//    Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
//    shortcutintent.putExtra("duplicate", false);
//    shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.shortcutname));
//    Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.icon);
//    shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
//    shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext(), EnterActivity.class));
//    sendBroadcast(shortcutintent);
//}

    }
}