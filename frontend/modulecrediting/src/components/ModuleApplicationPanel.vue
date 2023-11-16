<script setup>
import { ref } from "vue";

const moduleName = ref()
const university = ref()
const creditPoints = ref()
</script>

<template>
  <div>
    <Panel toggleable>

      <template #header>
        <h2 v-if="moduleName">{{ moduleName }}</h2>
        <h2 v-else>Neues Modul</h2>
      </template>

      <template #icons>
        <button class="p-panel-header-icon p-link mr-2">
          <span class="pi pi-trash"></span>
        </button>
      </template>

      <div class="main-container">
        <div class="module-container external">

          <h3>Anzurechnendes Modul:</h3>
          <div class="text-input-container">
            <p>Modulname:</p>
            <InputText type="text" v-model="moduleName"/>
          </div>
          <div class="text-input-container">
            <p>Universität:</p>
            <InputText type="text" v-model="university"/>
          </div>
          <div class="text-input-container">
            <p>Leistungspunkte:</p>
            <InputText type="text" v-model="creditPoints"/>
          </div>

        </div>
        <Divider layout="vertical" />
        <div class="module-container internal">

          <h3>Modul der Uni Leipzig:</h3>

        </div>

      </div>

    </Panel>
  </div>
</template>

<style scoped>
.main-container > * {
  display: inline-block;
}

.text-input-container > * {
  display: inline-block;
  padding: 10px;
}

.module-container {
  width: 48%;
}
</style>