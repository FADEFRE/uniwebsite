<!--
represents one application, used for StudyOfficeSelectionView
displays:
- id, course, status, creationDate
functionality:
- links to related detail view
-->

<script setup>
import { useRouter } from "vue-router";

const props = defineProps(['data', 'forward'])

const router = useRouter()

const openDetailView = () => {
  const routeData = router.resolve({name: props.forward, params: {id: props.data.id}})
  window.open(routeData.href, '_top')
}
</script>

<template>
  <div @click="openDetailView" class="overview-item">
    <div class="overview-child">
      <p>Vorgangsnummer: {{ props.data['id'] }}</p>
    </div>
    <div class="overview-child">
      <p>Studiengang: {{ props.data['courseLeipzig']['name'] }}</p>
    </div>
    <div class="overview-child">
      <p>Status: {{ props.data['fullStatus'] }}</p>
    </div>
    <div class="overview-child">
      <p>Erstellt: {{ props.data['creationDate'] }}</p>
    </div>
  </div>
</template>

<style scoped>
.overview-item {
  margin: 10px;

  background-color: #f4f4f4;

  border-width: 1px;
  border-radius: 5px;
  border-style: solid;
  border-color: #555555;
}

.overview-item:hover {
  background-color: #f8f8f8;
}

.overview-child {
  display: inline-block;
  padding: 5px 10px;
}
</style>