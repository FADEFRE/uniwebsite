<script setup>
import { ref, computed } from "vue";

const moduleName = ref()
const university = ref()
const creditPoints = ref()
const comment = ref()

const internalModules = [
    'Modellierung und Programmierung 1',
    'Praktikum Objektorientierte Programmierung',
    'Softwaretechnik',
    'Softwaretechnikpraktikum'
]
const internalModulesModel = ref(['placeholder'])
const selectedInternalModules = computed(
    () => internalModulesModel.value.filter((item) => item !== 'placeholder')
)

let selectedFiles = ref()
const handleFiles = (e) => {
  selectedFiles.value = e.target.files
}
const descriptionFile = computed(() => selectedFiles.value ? selectedFiles.value.item(0) : null)

defineExpose({
  moduleName, university, creditPoints, descriptionFile, selectedInternalModules, comment
})
</script>

<template>
  <div>

    <!-- External Module -->
    <div class="module-container" id="external">

      <h3 class="module-heading">Anzurechnendes Modul:</h3>
      <!-- Module Name -->
      <div class="text-input-container">
        <p>Modulname:</p>
        <InputText type="text" v-model="moduleName"/>
      </div>
      <!-- University -->
      <div class="text-input-container">
        <p>Universität:</p>
        <InputText type="text" v-model="university"/>
      </div>
      <!-- Credit Points -->
      <div class="text-input-container">
        <p>Leistungspunkte:</p>
        <InputText type="text" v-model="creditPoints"/>
      </div>
      <!-- File Upload -->
      <div class="file-input-container">
        <input type="file" accept=".pdf" @change="handleFiles">
      </div>

    </div>

    <!-- Internal Module -->
    <div class="module-container" id="internal">

      <h3 class="module-heading">Module der Uni Leipzig:</h3>
      <div class="internal-module-container">
        <!-- Internal Module Selection -->
        <div v-for="n in internalModulesModel.length" class="internal-module-dropdown-item">
          <Dropdown
              v-model="internalModulesModel[n]"
              :options="internalModules"
              :placeholder="n === 1 ? 'Modul wählen' : 'Weiteres Modul wählen'"
              class="module-dropdown"
          />
          <button
              v-if="n < internalModulesModel.length"
              icon="pi pi-trash"
              @click="internalModulesModel.splice(n, 1)"
          />
        </div>
        <!-- Comment -->
        <div>
          <textarea class="module -comment" v-model="comment"/>
        </div>
      </div>

    </div>

  </div>
</template>

<style scoped>
.module-container {
  display: inline-grid;
  width: 50%;
}

.module-heading {
  text-decoration-line: underline;
}

.text-input-container > * {
  display: inline-block;
  margin: 5px;
}

.text-input-container > p {
  width: 18%;
  text-align: center;
}

.internal-module-container {
  display: grid;
  justify-items: auto;
}

.internal-module-dropdown-item > * {
  margin: 5px;
}

.module-comment {
  padding: 5px;
  margin: 5px;

  width: 100%;
  height: 100%;
  font-size: 16px;
  color: #333333;

  box-shadow: 0 0 5px rgba(0, 128, 255, 0);
  border: 1px solid #aaaaaa;
  border-radius: 5px;
  transition: border-color 0.2s;
}

.module-comment:hover, .module-comment:focus {
  border-color: black;
}
</style>
