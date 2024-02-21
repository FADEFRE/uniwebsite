<script setup>
import ButtonDownload from "../components/ButtonDownload.vue";
import FileInput from "../components/FileInput.vue";
import ButtonLink from "../components/ButtonLink.vue";
import { url } from "@/scripts/url-config";

const jsonLink = `${url}/file/json/courses`
</script>

<template>
  <div class="admin-container-list">

    <div class="info-container">
      <h2>Hinweis zum Datei-Upload</h2>
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

    <div class="json-container">
      <h2>Konfiguration</h2>
      <div class="json-split-container">

        <div class="split-content">
          <h3>Download</h3>
          <a :href="jsonLink" target="_blank" download>
            <ButtonDownload>
              JSON herunterladen
            </ButtonDownload>
          </a>
          <small>Der aktuelle Stand als JSON-Konfigurationsdatei</small>
        </div>

        <div class="split-content">
          <h3>Upload</h3>
          <div>
            <FileInput :readonly="false">JSON auswählen</FileInput>
            <ButtonLink :red-button="true">JSON hochladen</ButtonLink>
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

.admin-container-list {
  @include verticalList(big);
  width: 100%;
}

.info-container {
  @include verticalListItem($white);
  @include basicContainer();
}

.json-container {
  @include basicContainer();
  @include verticalList(big);
  width: 100%;
}

.json-split-container {
  @include screenSplit();
  width: 100%;
}

.split-content {
  width: 100%;
}
</style>