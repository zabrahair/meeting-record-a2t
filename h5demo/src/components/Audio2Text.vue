<template>
  <div class="app-container">
    <el-input
      class="audio-text-content"
      type="textarea"
      :rows="10"
      placeholder="显示转换的文字"
      v-model="audioTxtContent"
      :readonly=isTextUnchangeable>
    </el-input>

    <audio-recorder
      class='meeting-recorder'
      :upload-url="uploadUrl"
      :attempts="3"
      :time="2"
      :start-record="onStartRecord"
      :stop-record="onStopRecord"
      :start-upload="onStartUpload"
      :successful-upload="onUploadSuccess"
      :failed-upload="onUploadFailed"/>
  </div>

</template>

<script>
  import { logObj } from '@/utils/logger'

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
      },
      onUploadSuccess: function(e){
        logObj(e, 'onUploadSuccess')
      },
      onUploadFailed: function(e){
        logObj(e, 'onUploadFailed')
      },
    }
  }
</script>

<style scoped>
  div {
    border: 1px solid black;
  }

  .app-container {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;

    width: 70em;
    height: 100%;

    border-color:red;
  }

  .audio-text-content {
    border-color: green;
  }

  .meeting-recorder{
    margin: 0em 0em 0em 10em;

  }
</style>
