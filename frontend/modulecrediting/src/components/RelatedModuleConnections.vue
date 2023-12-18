<script setup>
import { ref, onBeforeMount } from "vue";
import { getRelatedModuleConnections } from "@/scripts/axios-requests";

const props = defineProps(['moduleConnectionId'])

let relatedModuleConnections = ref(undefined)
onBeforeMount(() => {
  getRelatedModuleConnections(props.moduleConnectionId)
      .then(data => {
        relatedModuleConnections.value = data
        console.log(data)
      })
      .catch(error => console.log(error))
})

const generateModuleConnectionString = (connection) => {
  const separator = ' | '
  let conStr = connection['moduleApplication']['university']
  conStr += separator
  conStr += connection['moduleApplication']['name']
  conStr += ' -> '
  for (let [index, module] of connection['modulesLeipzig'].entries()) {
    conStr += module['moduleName']
    if (index < connection['modulesLeipzig'].length - 1) {
      conStr += ', '
    }
  }
  conStr += separator
  conStr += connection['application']['courseLeipzig']['name']
  conStr += separator
  conStr += connection['decisionFinal']
  conStr += separator
  conStr += connection['application']['decisionDate']
  return conStr
}
</script>

<template>
  <div v-if="relatedModuleConnections && relatedModuleConnections.length > 0">
    <hr>
    <h3>Ähnliche Modulanrechnungen:</h3>
    <div v-for="connection in relatedModuleConnections">
      <p>
        {{ generateModuleConnectionString(connection) }}
      </p>
    </div>
  </div>
</template>

<style scoped>

</style>