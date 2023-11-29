<!--
users can search for their applications status
displays:
- id text input
- see status button
functionality:
- tests on button click, if application with corresponding id exists
- if application exists, redirects to StatusDetailView
-->

<script setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { url } from "@/scripts/url-config";
import axios from "axios";

const router = useRouter()

const isInvalid = ref(false)

const id = ref()
const openDetailView = () => {
  axios.get(url + `/applications/${id.value}/exists`)
      .then(response => {
        if (response.data) {
          // routes to status detail view
          const routeData = router.resolve({name: 'statusDetail', params: {id: id.value}})
          window.open(routeData.href, '_top')
        } else {
          isInvalid.value = true
        }
      })
  // todo error catching
}
</script>

<template>
  <div class="view-container">
    <h2>Vorgangsnummer eingeben:</h2>
    <InputText type="text" v-model="id" :class="{ 'p-invalid': isInvalid }"/>
    <Button @click="openDetailView">Status einsehen</Button>
  </div>
</template>

<style scoped>

</style>