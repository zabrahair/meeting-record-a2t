<template>
  <div class="app-container">
    <el-input
      v-loading="isLoading"
      :element-loading-text="loadingMsg"
      class="audio-text-content"
      type="textarea"
      :rows="10"
      placeholder="显示转换的文字"
      v-model="audioTxtContent"
      :readonly=isTextUnchangeable>
    </el-input>

    <audio-recorder
      v-loading="isLoading"
      :element-loading-text="loadingMsg"
      class='meeting-recorder'
      :upload-url="uploadUrl"
      :attempts="3"
      :time="60"
      :start-record="onStartRecord"
      :stop-record="onStopRecord"
      :start-upload="onStartUpload"
      :successful-upload="onUploadSuccess"
      :failed-upload="onUploadFailed"/>
  </div>

</template>

<script>
  import { logObj } from '@/utils/logger'
  import { isNotEmptyArray } from '@/utils/index'

  // import AudioRecorder from 'vue-audio-recorder'
  // import Vue from 'vue'
  // Vue.use(AudioRecorder)

  export default {
    name: "Audio2Text",
    data () {
      return {
        msg: 'Welcome to Your Vue.js App',
        audioTxtContent: '',
        isTextUnchangeable: true,
        uploadUrl: 'http://localhost:8080/api/file/upload',
        audioConvertData: {},
        isLoading: false,
        loadingMsg: "语音转换处理中",
      }
    },
    methods: {
      onStartRecord: function(e){
        logObj(e, 'onStartRecord')
      },
      onStopRecord: function(e){
        logObj(e, 'onStopRecord')
      },
      onStartUpload: function(e){
        logObj(e, 'onStartUpload')
        this.isLoading = true;
      },
      onUploadSuccess: function(e){
        this.audioConvertData = e.data
        logObj(this.audioConvertData, "result data")
        logObj(isNotEmptyArray(this.audioConvertData), "is Array")
        if (isNotEmptyArray(this.audioConvertData)){
          for(var i = 0; i < this.audioConvertData.length; i++){
            logObj(this.audioConvertData[i].onebest, "oneBest")
            this.audioTxtContent += `${i==0?'':'。'} ${this.audioConvertData[i].onebest}`;
          }
        }
        this.isLoading = false;
        // logObj(e, 'onUploadSuccess')
      },
      onUploadFailed: function(e){
        logObj(e, 'onUploadFailed')
        this.isLoading = false;
      },
    }
  }
</script>

<style scoped>
  /*div {*/
    /*border: 1px solid black;*/
  /*}*/

  .app-container {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;

    width: 100%;
    height: 100%;

    /*border-color:red;*/
  }

  .audio-text-content {
    /*border-color: green;*/
  }

  .meeting-recorder{
    margin: 0em 0em 0em 10em;

  }
</style>
