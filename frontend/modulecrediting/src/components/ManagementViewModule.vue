<script setup>
import { ref, computed, onBeforeMount } from "vue";
import ManagementListElement from "./ManagementListElement.vue";
import LoadingContainer from "@/components/LoadingContainer.vue";
import { getModulesNameCode, deleteModuleLeipzig, putUpdateModuleLeipzig } from "@/scripts/axios-requests";

const modules = ref();
const searchString = ref('');

const exists = ref(false)

onBeforeMount(() => {
    getModulesNameCode()
        .then(data => modules.value = data)
})

const filteredModules = computed(() => {
    if (searchString.value === '') return modules.value;

    return modules.value.filter(singleModule => {
        return singleModule.name.toLocaleLowerCase().includes(searchString.value.toLocaleLowerCase());
    })
})

const triggerEditModuleLeipzig = (existsVar, oldName, newName, newCode) => {
  putUpdateModuleLeipzig(oldName, newName, newCode)
      .then(_ => location.reload())
      .catch(error => {
        if (error.response.status === 409) {
          existsVar.value = true
        }
      })
}

const triggerDeleteModuleLeipzig = (module) => {
    deleteModuleLeipzig(module)
        .then(_ => location.reload())
}
</script>

<template>
  <div class="view-module-container">
    <h2>ALLE MODULE</h2>

    <div v-if="modules" class="view-module-list">

      <div class="search-container">
        <InputText v-model="searchString" placeholder="Modul suchen"></InputText>
        <img src="@/assets/icons/SearchIcon.svg" class="search-icon">
      </div>
      <div v-for="singleModule in filteredModules" class="module-list">
        <ManagementListElement
            :key="singleModule['name']"
            :name="singleModule['name']"
            :code="singleModule['code']"
            :edit-callback="triggerEditModuleLeipzig"
            :delete-callback="triggerDeleteModuleLeipzig"
            exists-text="Modulname oder Modulcode existiert bereits"
        />
      </div>
      <small class="helper-text">Es gibt insgesamt {{ modules.length }} Module.</small>

    </div>
    <div v-else class="view-module-loading-container">
      <LoadingContainer />
    </div>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.view-module-container {
    @include basicContainer();
}

.view-module-list,
.view-module-loading-container {
    @include verticalList(s);
}

.search-container {
    @include searchFieldContainer();
}

.module-list {
  @include verticalList(small)
}
</style>