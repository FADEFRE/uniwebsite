<script setup>
import { ref } from "vue";
import ButtonDownload from "@/components/button/ButtonDownload.vue";
import FileInput from "@/components/util/FileInput.vue";
import ButtonLink from "@/components/button/ButtonLink.vue";
import { url } from "@/config/url-config";
import { postJsonConfig } from "@/requests/module-course-requests";

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
          Durch das Hochladen einer JSON-Konfigurationsdatei werden alle Daten verändert,
          sodass diese der hochgeladenen Konfigurationsdatei entsprechen.
          Das bedeutet explizit: nicht angegebene Module / Kurse werden gelöscht,
          neu hinzugefügte Module / Kurse werden erstellt, die Zuordnung der Module zu Studiengängen wird verändert.
          Diese Änderungen können nicht automatisch rückgängig gemacht werden.
        </p>
        <p>
          Um Änderungen durchzuführen kann der aktuelle Stand als JSON-Konfigurationsdatei heruntergeladen werden,
          modifiziert werden und dann erneut hochgeladen werden.
        </p>
      </div>

      <div class="buttons-container">
          <div class="download-container">
          <h3>Download</h3>
          
          <a :href="jsonLink" target="_blank" download>
            <ButtonDownload>
              JSON herunterladen
            </ButtonDownload>
          </a>
          <small>Der aktuelle Stand als JSON-Konfigurationsdatei</small>
        </div>

        <div class="upload-container">
          <h3>Upload</h3>
          <div class="file-button-container">
            <FileInput :readonly="false" type="json" ref="fileInput">JSON auswählen</FileInput>
            <ButtonLink :red-button="true" @click="uploadJson">JSON hochladen</ButtonLink>
          </div>
          
          <small>Ändert die Konfiguration gemäß der ausgewählten Datei. Bitte Hinweis beachten.</small>
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
  @include breakpoint(m) {
    flex-direction: column;
  }
}

.info-container, .buttons-container {
  @include verticalList(s);
  width: 50%;

  @include breakpoint(m) {
    width: 100%;
  }
}

.upload-container, .download-container {
  @include verticalList(s);
  width: 100%;
}


.file-button-container {
  display: flex;
  align-items: center;
  gap: spacing(m);
}
</style>