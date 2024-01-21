<script setup>
import { useRoute } from "vue-router";
import { ref, computed, onBeforeMount } from "vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import AdministrativePanel from "@/components/AdministrativePanel.vue";
import ButtonLink from "@/components/ButtonLink.vue";
import {
  getApplicationById, getModulesByCourse,
  getUpdateStatusAllowed, updateStatus, putApplicationStudyOffice
} from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import ApplicationConnectionLinks from "@/components/ApplicationConnectionLinks.vue";

const route = useRoute()
const id = route.params.id
const connectionHighlightId = route.params.connection
const type = route.meta['authType']
const readonly = ref(true)

const applicationData = ref()
const moduleOptions = ref([])
const passOnStatus = ref('NOT_ALLOWED')

onBeforeMount(() => {
  getApplicationById(id)
    .then(data => {
      // applicationData
      data['modulesConnections'].sort((a, b) => a.id - b.id)
      applicationData.value = data
      // readonly
      if (type === 'study-office' && (data['fullStatus'] === 'NEU' || data['fullStatus'] === 'STUDIENBÜRO')) {
        readonly.value = false
      } else if (type === 'chairman' && data['fullStatus'] !== 'ABGESCHLOSSEN') {
        readonly.value = false
      }
      return data
    })
    .then(data => {
      return getModulesByCourse(data['courseLeipzig']['name'])
    })
    .then(modules => {
      moduleOptions.value = modules
    })
    .then(_ => {
      return getUpdateStatusAllowed(id)
    })
    .then(updateAllowed => {
      passOnStatus.value = updateAllowed
    })
})

const moduleConnections = ref()

const connectionsData = computed(() => {
  const dataArray = []
  if (moduleConnections.value) {
    for (let connection of moduleConnections.value) {
      const connectionObj = {}
      connectionObj['id'] = connection.id
      connectionObj['externalModules'] = connection.externalModules.map(m => m.name)
      dataArray.push(connectionObj)
    }
  }
  return dataArray
})

const collapseAll = () => {
  for (let panel of moduleConnections.value) {
    panel.setCollapsed(true)
  }
}

const unCollapseAll = () => {
  for (let panel of moduleConnections.value) {
    panel.setCollapsed(false)
  }
}

const unsaved = ref(false)

const setUnsaved = () => {
  unsaved.value = true
}

const discardChanges = () => {
  location.reload()
}

const saveChanges = () => {
  if (type === 'study-office') {
    putApplicationStudyOffice(id, applicationData.value['courseLeipzig']['name'], moduleConnections.value)
        .then(_ => location.reload())
  }
}

const triggerPassOn = () => {
  if (passOnStatus) updateStatus(id).then(_ => location.reload())
}
</script>

<template>
  <!-- request pending -->
  <div v-if="!applicationData" class="main">
    <p>Lade Daten ...</p>
  </div>

  <div v-else class="main">

    <ApplicationConnectionLinks :connections-data="connectionsData" />

    <div class="administrative-detail-container">
      <ApplicationOverview :creation-date="parseRequestDate(applicationData['creationDate'])"
        :last-edited-date="parseRequestDate(applicationData['lastEditedDate'])"
        :decision-date="parseRequestDate(applicationData['decisionDate'])" :id="applicationData['id']"
        :course="applicationData['courseLeipzig']['name']" :status="applicationData['fullStatus']" />

      <div v-for="connection in applicationData['modulesConnections']">

        <AdministrativePanel :type="type" :readonly="readonly" :selectable-modules="moduleOptions"
          :connection-data="connection" ref="moduleConnections"
          :class="{ 'connection-highlight': connection.id == connectionHighlightId }" :id="connection.id"
          @change="setUnsaved" />

      </div>

      <div v-if="!readonly" class="saving-buttons-container">
        <ButtonLink @click="discardChanges">Änderungen verwerfen</ButtonLink>
        <ButtonLink @click="saveChanges">Speichern</ButtonLink>
      </div>

      <div v-if="unsaved">
        <Button class="unsaved-button">
          <img src="@/assets/icons/NotSaved.svg">
        </Button>
      </div>

      <div>
        <Button @click="collapseAll" class="collapse-expand-button">
          <img src="@/assets/icons/CollapseAll.svg">
        </Button>
        <Button @click="unCollapseAll" class="collapse-expand-button">
          <img src="@/assets/icons/ExpandAll.svg">
        </Button>
      </div>

      <div v-if="!readonly">
        <ButtonLink v-if="passOnStatus === 'NOT_ALLOWED'" :disabled="true">Weitergeben</ButtonLink>
        <ButtonLink v-else-if="passOnStatus === 'PASSON'" :primaryButton="true" @click="triggerPassOn">Weitergeben</ButtonLink>
        <ButtonLink v-else-if="passOnStatus === 'REJECT'" :primary-button="true" @click="triggerPassOn">Zurückweisen</ButtonLink>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
  @include main();
}

.administrative-detail-container {
  @include verticalList(small);
  width: 100%;
}

.connection-highlight {
  border-left: 1rem solid $dark-gray;
}

.saving-buttons-container {
  display: flex;
  width: 100%;
  justify-content: space-around;
}

.unsaved-button {
  background-color: $red;
  width: 3.125rem;
  height: 3.125rem;
}
.unsaved-button-text {
  color: $white;
}

.collapse-expand-button {
  width: 3.125rem;
  height: 3.125rem;
}
</style>