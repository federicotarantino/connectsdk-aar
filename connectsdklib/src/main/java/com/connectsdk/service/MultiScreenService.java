/*
 * SmartViewService
 * Connect SDK
 *
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 23 Feb 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.connectsdk.service;

import android.net.Uri;

import com.connectsdk.core.ImageInfo;
import com.connectsdk.core.MediaInfo;
import com.connectsdk.core.Util;
import com.connectsdk.discovery.DiscoveryFilter;
import com.connectsdk.service.capability.CapabilityMethods;
import com.connectsdk.service.capability.MediaControl;
import com.connectsdk.service.capability.MediaPlayer;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;
import com.connectsdk.service.command.URLServiceSubscription;
import com.connectsdk.service.config.ServiceConfig;
import com.connectsdk.service.config.ServiceDescription;
import com.connectsdk.service.sessions.LaunchSession;
import com.samsung.multiscreen.Error;
import com.samsung.multiscreen.Player;
import com.samsung.multiscreen.Result;
import com.samsung.multiscreen.Service;
import com.samsung.multiscreen.VideoPlayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiScreenService extends DeviceService implements MediaPlayer, MediaControl, VolumeControl {

  public static final String ID = "MultiScreen";

  private Service service;
  private VideoPlayer app;

  public MultiScreenService(ServiceDescription serviceDescription, ServiceConfig serviceConfig) {
    super(serviceDescription, serviceConfig);

    service = (Service) serviceDescription.getDevice();

  }

  @Override
  public String getServiceName() {
    return ID;
  }

  public static DiscoveryFilter discoveryFilter() {
    return new DiscoveryFilter(ID, "urn:samsung.com:service:MultiScreenService:1");
  }

  @Override
  public CapabilityPriorityLevel getPriorityLevel(Class<? extends CapabilityMethods> clazz) {
    if (clazz.equals(MediaPlayer.class)) {
      return getMediaPlayerCapabilityLevel();
    }
    else if (clazz.equals(MediaControl.class)) {
      return getMediaControlCapabilityLevel();
    }
    else if (clazz.equals(VolumeControl.class)) {
      return getVolumeControlCapabilityLevel();
    }
    return CapabilityPriorityLevel.NOT_SUPPORTED;
  }

  @Override
  public boolean isConnected() {
    return service != null && app != null;
  }

  @Override
  public void connect() {
    app = service.createVideoPlayer("IPTVCast");
    VideoPlayer.OnVideoPlayerListener videoPlayerListener = new VideoPlayer.OnVideoPlayerListener() {
      @Override
      public void onBufferingStart() {
        //Do something here..
      }

      @Override
      public void onBufferingComplete() {
        //Do something here..
      }

      @Override
      public void onBufferingProgress(int progress) {
        //Do something here..
      }

      @Override
      public void onCurrentPlayTime(int progress) {
        //Do something here..
      }

      @Override
      public void onStreamingStarted(int i) {

      }

      @Override
      public void onStreamCompleted() {

      }

      @Override
      public void onPlayerInitialized() {

      }

      @Override
      public void onPlayerChange(String s) {

      }

      @Override
      public void onPlay() {

      }

      @Override
      public void onPause() {

      }

      @Override
      public void onStop() {

      }

      @Override
      public void onForward() {

      }

      @Override
      public void onRewind() {

      }

      @Override
      public void onMute() {

      }

      @Override
      public void onUnMute() {

      }

      @Override
      public void onNext() {

      }

      @Override
      public void onPrevious() {

      }

      @Override
      public void onControlStatus(int i, Boolean aBoolean, Player.RepeatMode repeatMode) {

      }

      @Override
      public void onVolumeChange(int i) {

      }

      @Override
      public void onAddToList(JSONObject jsonObject) {

      }

      @Override
      public void onRemoveFromList(JSONObject jsonObject) {

      }

      @Override
      public void onClearList() {

      }

      @Override
      public void onGetList(JSONArray jsonArray) {

      }

      @Override
      public void onRepeat(Player.RepeatMode repeatMode) {

      }

      @Override
      public void onCurrentPlaying(JSONObject jsonObject, String s) {

      }

      @Override
      public void onApplicationResume() {

      }

      @Override
      public void onApplicationSuspend() {

      }

      @Override
      public void onError(Error error) {

      }

    };

    app.addOnMessageListener(videoPlayerListener);
    reportConnected(true);
  }

  @Override
  public void disconnect() {
    if(app!=null) {
      app.disconnect();
      app = null;
    }
  }

  @Override
  public MediaControl getMediaControl() {
    return this;
  }

  @Override
  public CapabilityPriorityLevel getMediaControlCapabilityLevel() {
    return CapabilityPriorityLevel.HIGH;
  }

  @Override
  public void play(final ResponseListener<Object> listener) {
    app.play();
    Util.postSuccess(listener, null);
  }

  @Override
  public void pause(final ResponseListener<Object> listener) {
    app.pause();
    Util.postSuccess(listener, null);
  }

  @Override
  public void stop(final ResponseListener<Object> listener) {
    app.stop();
    Util.postSuccess(listener, null);
  }

  @Override
  public void rewind(ResponseListener<Object> listener) {
    app.rewind();
    Util.postSuccess(listener, null);
  }

  @Override
  public void fastForward(ResponseListener<Object> listener) {
    app.forward();
    Util.postSuccess(listener, null);
  }

  @Override
  public void previous(ResponseListener<Object> listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
  }

  @Override
  public void next(ResponseListener<Object> listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
  }

  @Override
  public void seek(final long position, final ResponseListener<Object> listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
  }

  @Override
  public void getDuration(final DurationListener listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
  }

  @Override
  public void getPosition(final PositionListener listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
  }

  @Override
  public void getPlayState(PlayStateListener listener) {
    //
  }

  @Override
  public MediaPlayer getMediaPlayer() {
    return this;
  }

  @Override
  public CapabilityPriorityLevel getMediaPlayerCapabilityLevel() {
    return CapabilityPriorityLevel.HIGH;
  }

  @Override
  public void getMediaInfo(MediaInfoListener listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
  }

  @Override
  public ServiceSubscription<MediaInfoListener> subscribeMediaInfo(MediaInfoListener listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
    return null;
  }

  @Override
  public void displayImage(String url, String mimeType, String title,
                           String description, String iconSrc, LaunchListener listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
  }

  @Override
  public void displayImage(MediaInfo mediaInfo, LaunchListener listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
  }

  @Override
  public void playMedia(String url, String mimeType, String title,
                        String description, String iconSrc, boolean shouldLoop,
                        final LaunchListener listener) {

    if(app!=null){
      app.playContent(Uri.parse(url), title, Uri.parse(iconSrc), new Result<Boolean>() {
        @Override
        public void onSuccess(Boolean aBoolean) {
          LaunchSession launchSession = LaunchSession.launchSessionForAppId("IPTVCast");
          launchSession.setService(MultiScreenService.this);
          launchSession.setSessionId(service.getId());
          launchSession.setSessionType(LaunchSession.LaunchSessionType.Media);
          Util.postSuccess(listener, new MediaLaunchObject(launchSession, MultiScreenService.this));
        }

        @Override
        public void onError(Error error) {
          Util.postError(listener, ServiceCommandError.getError((int) error.getCode()));
        }
      });
    }
  }

  @Override
  public void playMedia(MediaInfo mediaInfo, boolean shouldLoop, LaunchListener listener) {
    String mediaUrl = null;
    String mimeType = null;
    String title = null;
    String desc = null;
    String iconSrc = null;

    if (mediaInfo != null) {
      mediaUrl = mediaInfo.getUrl();
      mimeType = mediaInfo.getMimeType();
      title = mediaInfo.getTitle();
      desc = mediaInfo.getDescription();

      if (mediaInfo.getImages() != null && mediaInfo.getImages().size() > 0) {
        ImageInfo imageInfo = mediaInfo.getImages().get(0);
        iconSrc = imageInfo.getUrl();
      }
    }

    playMedia(mediaUrl, mimeType, title, desc, iconSrc, shouldLoop, listener);
  }

  @Override
  public void closeMedia(final LaunchSession launchSession, final ResponseListener<Object> listener) {
    stop(listener);
  }

  @Override
  public VolumeControl getVolumeControl() {
    return this;
  }

  @Override
  public CapabilityPriorityLevel getVolumeControlCapabilityLevel() {
    return CapabilityPriorityLevel.HIGH;
  }

  @Override
  public void volumeUp(final ResponseListener<Object> listener) {
    app.volumeUp();
    Util.postSuccess(listener, null);
  }

  @Override
  public void volumeDown(final ResponseListener<Object> listener) {
    app.volumeDown();
    Util.postSuccess(listener, null);
  }

  @Override
  public void setVolume(final float volume, final ResponseListener<Object> listener) {
    app.setVolume((int) volume);
    Util.postSuccess(listener, null);
  }

  @Override
  public void getVolume(VolumeListener listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
  }

  @Override
  public void setMute(final boolean isMute, final ResponseListener<Object> listener) {
    if(isMute) app.mute();
    else app.unMute();
    Util.postSuccess(listener, null);
  }

  @Override
  public void getMute(final MuteListener listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
  }

  @Override
  public ServiceSubscription<VolumeListener> subscribeVolume(VolumeListener listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
    return null;
  }

  @Override
  public ServiceSubscription<MuteListener> subscribeMute(MuteListener listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
    return null;
  }

  @Override
  protected void updateCapabilities() {
    List<String> capabilities = new ArrayList<>();

    Collections.addAll(capabilities, MediaPlayer.Capabilities);

    Collections.addAll(capabilities, VolumeControl.Capabilities);

    capabilities.add(Play);
    capabilities.add(Pause);
    capabilities.add(Stop);
    capabilities.add(Duration);
    capabilities.add(Seek);
    capabilities.add(Position);
    capabilities.add(PlayState);
    capabilities.add(PlayState_Subscribe);

    setCapabilities(capabilities);
  }

  //////////////////////////////////////////////////
  //      Device Service Methods
  //////////////////////////////////////////////////
  @Override
  public boolean isConnectable() {
    return true;
  }

  @Override
  public ServiceSubscription<PlayStateListener> subscribePlayState(PlayStateListener listener) {
    Util.postError(listener, ServiceCommandError.notSupported());
    return null;
  }

  @Override
  public void unsubscribe(URLServiceSubscription<?> subscription) {
    //
  }

}
