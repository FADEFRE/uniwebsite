<script setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'

const router = useRouter()

const id = ref()
const openDetailView = () => {
  const routeData = router.resolve({name: 'statusDetail', params: {id: id.value}})
  window.open(routeData.href, '_top')
}
</script>

<template>
  <div class="view-container">
    <h2>Vorgangsnummer eingeben:</h2>
    <InputText type="text" v-model="id"/>
    <Button @click="openDetailView">Status einsehen</Button>
  </div>
</template>

<style scoped>

</style>