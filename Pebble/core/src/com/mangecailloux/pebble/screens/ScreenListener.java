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
package com.mangecailloux.pebble.screens;

public interface ScreenListener 
{
   public void onDispose ();
   public void onResize (int width, int height);
   public void onFirstActivation();
   public void onActivation();
   public void onDeactivation();
   public void onPause ();
   public void onResume ();
   
   public class ScreenListenerAdapter implements ScreenListener
   {
	@Override
	public void onDispose() {}

	@Override
	public void onResize(int width, int height) {}

	@Override
	public void onFirstActivation() {}

	@Override
	public void onActivation() {}

	@Override
	public void onDeactivation() {}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}
   
   }
}
