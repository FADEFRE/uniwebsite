<script setup>
import { useRoute, useRouter } from 'vue-router';
import { onBeforeMount } from 'vue';
import ConfirmationContainer from '../components/ConfirmationContainer.vue';
import ButtonLink from "@/components/ButtonLink.vue";
import ButtonDownload from '../components/ButtonDownload.vue';
import { url } from '@/scripts/url-config';

const route = useRoute()
const router = useRouter()
let id = undefined
let pdfDataLink = undefined

onBeforeMount(() => {
  id = route.params.id;
  pdfDataLink = `${url}/file/pdf-documents/application/${id}`
})

const openDetailView = () => {
  router.push({ name: 'statusDetail', params: { id: id } })
}

const openPdf = () => {
  window.open(pdfDataLink, '_blank');
}


</script>

<template>
  <div class="main centered">
    <ConfirmationContainer :id="id">
      <ButtonDownload @click="openPdf">
        Antrag herunterladen
      </ButtonDownload>
      <ButtonLink @click="openDetailView" class="status-button">Status einsehen</ButtonLink>
    </ConfirmationContainer>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
</style>
