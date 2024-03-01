<script setup>
import { useClipboard } from '@vueuse/core'
import { useRoute, useRouter } from 'vue-router';
import { computed, onBeforeMount } from 'vue';
import CopyIcon from "@/assets/icons/CopyIcon.vue";
import ButtonLink from "@/components/button/ButtonLink.vue";
import ButtonDownload from '@/components/button/ButtonDownload.vue';
import { url } from '@/config/url-config';

const route = useRoute()
const router = useRouter()
let id = undefined
let pdfDataLink = undefined

onBeforeMount(() => {
  id = route.params.id;
  pdfDataLink = `${url}/file/pdf-documents/application/${id}`
})

// copy
const { copy, copied, isSupported } = useClipboard()

const copyId = () => {
  copy(id);
}

// formattedId
function formatId(id) {
  return id.replace(/(\d)(?=(\d{1,5})+$)/g, '$1-');
}

const formattedId = computed(() => formatId(id))

// open functions
const openDetailView = () => {
  router.push({ name: 'statusDetail', params: { id: id } })
}

const openPdf = () => {
  window.open(pdfDataLink, '_blank');
}
</script>

<template>
  <div class="main centered">
    <h1 class="screen-reader-only">Bestätigung des Antrags</h1>
    <div class="confirmation-container">
      <div class="id-section">
        <div class="id-container">
          <h2 class="id">{{ formattedId }}</h2>
          <div @click="copyId" @keydown.enter="copyId" class="copy-icon-container" tabindex="0" aria-label="Kopieren der Vorgangsnummer">
            <CopyIcon v-if="isSupported" :disabled="copied" />
          </div>
        </div>
        <p class="description-text">
          Notieren Sie sich die Vorgangsnummer. Diese ist notwendig, um später den Status Ihres Antrags einsehen zu können.
        </p>
      </div>
      <div class="button-container">
        <ButtonDownload @click="openPdf">
          Antrag herunterladen
        </ButtonDownload>
        <ButtonLink @click="openDetailView" class="status-button">Status einsehen</ButtonLink>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.confirmation-container {
  @include singleContainer();
}

.button-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: spacing(m);
}

.id-section {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: spacing(m);
  width: min-content;
}

.id-container {
  position: relative;
  width: max-content;
  padding: spacing(m) spacing(xl);
  background-color: $gray;
  border: solid 1px $dark-gray;
}
.id {
  font-size: 2rem;
  font-weight: 800;
  letter-spacing: 0.5rem;
}
.copy-icon-container {
  position: absolute;
  top: 0;
  right: 0;
  padding: spacing(s);

  &:focus {
    outline: 2.5px solid $dark-gray-hover;
  }
}

.description-text {
  text-align: center;
}
</style>
