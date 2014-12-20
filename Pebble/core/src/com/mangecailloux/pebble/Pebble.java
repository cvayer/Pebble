/*******************************************************************************
 * Copyright 2013 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.mangecailloux.pebble;

import com.mangecailloux.pebble.ads.AdsManager;
import com.mangecailloux.pebble.assets.AssetsManager;
import com.mangecailloux.pebble.audio.MusicManager;
import com.mangecailloux.pebble.audio.SoundManager;
import com.mangecailloux.pebble.language.LanguagesManager;
import com.mangecailloux.pebble.vibration.VibrationManager;
import com.mangecailloux.pebble.webpage.WebPageManager;

/** <p>Environment class holding references to the various managers of the Pebble framework.
 *  The references are held in public static fields similar to the Gdx class in libGdx. Do not mess with this! 
 *  It is your responsibility to keep things thread safe. 
 *  </p>
 *  <p>
 *  You can access {@link AssetsManager}, {@link MusicManager}, {@link SoundManager}, {@link LanguagesManager},
 * {@link VibrationManager}, {@link AdsManager} and {@link WebPageManager} instances. 
 *  </p>
 *  <p>
 *  All instances are created by the {@link ScreenManager} just before its init() method. 
 *  They are set to null in it's dispose() method.
 *  So they should be safe to access anywhere in your game except for the ScreenManager ctor.
 *  </p>
 *  @author clement.vayer
 */
public class Pebble 
{
	public static MusicManager 		musics;
	public static SoundManager 		sounds;
	public static LanguagesManager 	languages;
	public static VibrationManager 	vibrations;
	public static AdsManager		ads;
	public static WebPageManager 	webpages;
	public static AssetsManager		assets;
}
