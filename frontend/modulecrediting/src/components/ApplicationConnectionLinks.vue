<!--
displays link overview for one application
props:
  - connectionData, array of objects, each having id (Number),
    externalModules (array of Strings), internalModules (array of Strings) property
-->

<script setup>
import PanelHeader from "@/components/PanelHeader.vue";

const props = defineProps({
  connectionsData: {
    required: true,
    type: Array,
    validator(value) {
      return value.every(obj => obj.id && obj.externalModules && obj.internalModules)
    }
  }
})
</script>

<template>
  <div>

    <h2>Übersicht</h2>

    <div>
      <div v-for="connection in connectionsData">
        <a :href="'#'+connection.id">
          <PanelHeader
              :external-modules="connection.externalModules"
              :internal-modules="connection.internalModules"
          />
        </a>
      </div>
    </div>

  </div>
</template>

<style scoped lang="scss">

</style>