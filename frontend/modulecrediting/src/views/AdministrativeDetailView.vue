<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref, computed, onBeforeMount } from "vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import AdministrativePanel from "@/components/AdministrativePanel.vue";
import ButtonLink from "@/components/ButtonLink.vue";
import {
  getApplicationById, getModulesByCourse,
  getUpdateStatusAllowed, updateStatus, putApplicationStudyOffice, putApplicationChairman
} from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import ApplicationConnectionLinks from "@/components/ApplicationConnectionLinks.vue";

const route = useRoute()
const router = useRouter();
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

const scrollTop = () => {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}

const unsaved = ref(false)

const setUnsaved = () => {
  unsaved.value = true
}

const discardChanges = () => {
  location.reload()
}

const checkValidity = () => {
  return moduleConnections.value.map(c => c.checkValidity()).every(Boolean)
}

const saveChanges = () => {
  if (checkValidity()) {
    if (type === 'study-office') {
      putApplicationStudyOffice(id, applicationData.value['courseLeipzig']['name'], moduleConnections.value)
          .then(_ => location.reload())
    } else if (type === 'chairman') {
      putApplicationChairman(id, applicationData.value['courseLeipzig']['name'], moduleConnections.value)
          .then(_ => location.reload())
    }
  }
}

const triggerPassOn = () => {
  if (passOnStatus) updateStatus(id).then(_ => location.reload())
}

</script>

<template>
  <div v-if="applicationData" class="main">

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

      <div v-if="!readonly" class="save-controlls-container">
        <ButtonLink @click="saveChanges">Speichern</ButtonLink>
        <ButtonLink @click="discardChanges">Änderungen verwerfen</ButtonLink>
      </div>

    </div>

    <div class="mid-right-fixed-container">
      <div class="unsaved-notification" v-if="unsaved">
        <img src="@/assets/icons/NotSaved.svg">
      </div>

      <Button @click="collapseAll" class="collapse-expand-button">
        <img src="@/assets/icons/CollapseAll.svg">
      </Button>
      <Button @click="unCollapseAll" class="collapse-expand-button">
        <img src="@/assets/icons/ExpandAll.svg">
      </Button>

      <Button @click="scrollTop" class="move-top-button">
        <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
      </Button>
    </div>

    <div v-if="!readonly">
      <ButtonLink v-if="passOnStatus === 'NOT_ALLOWED'" :disabled="true" :fixed="true">Weitergeben</ButtonLink>
      <ButtonLink v-else-if="passOnStatus === 'PASSON'" :fixed="true" @click="triggerPassOn">Weitergeben
      </ButtonLink>
      <ButtonLink v-else-if="passOnStatus === 'REJECT'" :fixed="true" @click="triggerPassOn">Zurückweisen
      </ButtonLink>
    </div>
  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
  @include main();

  @media only screen and (max-width: 1300px) {
    gap: 0; // remove gap for application modules connection links
  }
}

.administrative-detail-container {
  @include verticalList(small);
  width: 100%;
  overflow: hidden;
}


.save-controlls-container {
  display: flex;
  width: 100%;
  justify-content: flex-start;
  flex-wrap: wrap;
  gap: 0.625rem 1rem;

  @media only screen and (max-width: 700px) {
    padding: 0 0.625rem;
  }
}



.mid-right-fixed-container {
  @include verticalList(small);

  display: flex;
  flex-direction: column;
  align-items: flex-end;

  position: fixed;
  bottom: 5.625rem;
  right: 1rem;
}


.unsaved-notification {
  background-color: $red;
  width: 3.125rem;
  height: 3.125rem;

  display: flex;
  justify-content: center;
  align-items: center;
}

.collapse-expand-button {
  width: 3.125rem;
  height: 3.7rem;

  @media only screen and (max-width: 950px) {
    display: none;
  }
}

.move-top-button {
  width: 3.125rem;
  height: 3.125rem;

  & .arrow-icon {
    transform: rotate(180deg);
    width: 18px;
    height: 12px;
  }
}
</style>