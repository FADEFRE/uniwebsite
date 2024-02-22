<script setup>
import { ref } from "vue";
import ButtonDownload from "../components/ButtonDownload.vue";
import FileInput from "../components/FileInput.vue";
import ButtonLink from "../components/ButtonLink.vue";
import { url } from "@/scripts/url-config";
import { postJsonConfig } from "@/scripts/axios-requests";

const jsonLink = `${url}/file/json/courses`

const fileInput = ref()

const uploadJson = () => {
  if (fileInput.value.checkValidity().value) {
    postJsonConfig(fileInput.value.selectedFile)
      .then(_ => location.reload())
  }
}
</script>

<template>
  <div class="config-container">
    <h2>Konfiguration</h2>

    <div class="screen-split-container">


      <div class="info-container">
        <h3>Hinweis zum Datei-Upload</h3>
        <p>
          Durch das Hochladen einer JSON-Konfigurationsdatei werden alle Daten verändert, sodass diese der hochgeladenen
          Konfigurationsdatei entsprechen. Das umfasst also explizit:
          Liste aller Studiengänge, Liste aller Module, Zuordnung der Module zu Studiengängen
          Diese Änderungen können nicht automatisch rückgängig gemacht werden.
        </p>
        <p>
          Um Änderungen durchzuführen kann der aktuelle Stand als JSON-Konfigurationsdatei heruntergeladen werden,
          modifiziert werden und dann erneut hochgeladen werden.
        </p>
      </div>

      <div class="buttons-container">
        <h3>Konfigurationstools</h3>
        <div class="download-container">
          <h4>Download</h4>
          <a :href="jsonLink" target="_blank" download>
            <ButtonDownload>
              JSON herunterladen
            </ButtonDownload>
          </a>
          <small>Der aktuelle Stand als JSON-Konfigurationsdatei</small>
        </div>

        <div class="upload-container">
          <h4>Upload</h4>
          <div class="button-file-input-container">
            <FileInput :readonly="false" type="json" ref="fileInput">JSON auswählen</FileInput>
            <ButtonLink class="upload-button" :red-button="true" @click="uploadJson">JSON hochladen</ButtonLink>
          </div>
          <small>Ändert die Konfiguration gemäß der ausgewählten Datei. Bitte obigen Hinweis beachten.</small>
        </div>

      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.config-container {
  @include basicContainer();
}

.screen-split-container {
  @include screenSplit();
  gap: spacing(xl);
}

.info-container {
  @include verticalList(s);
  width: 50%;
}

.buttons-container {
  @include verticalList(s);
  width: 50%;
}

.button-file-input-container {
  display: flex;
  width: 100%;
  gap: spacing(s);
  align-items: center;
}
</style>