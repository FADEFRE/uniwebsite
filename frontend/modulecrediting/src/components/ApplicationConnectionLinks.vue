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
      return value.every(obj => obj.id && obj.externalModules)
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
          <div>
            <p>{{ connection.externalModules.join(', ') }}</p>
          </div>
          <div>
            <img src="@/assets/icons/ArrowRed.svg" alt="">
          </div>
        </a>
      </div>
    </div>

  </div>
</template>

<style scoped lang="scss">

</style>