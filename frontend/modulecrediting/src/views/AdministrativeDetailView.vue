<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref, computed, onBeforeMount } from "vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import AdministrativePanel from "@/components/AdministrativePanel.vue";
import ApplicationConnectionLinks from "@/components/ApplicationConnectionLinks.vue";
import ButtonLink from "@/components/ButtonLink.vue";
import ApplicationControl from "../assets/icons/ApplicationControl.vue";
import MoveTop from "../assets/icons/MoveTop.vue";
import NotSavedIcon from "../assets/icons/NotSavedIcon.vue";
import {
  getApplicationById, getModulesByCourse,
  getUpdateStatusAllowed, updateStatus, putApplicationStudyOffice, putApplicationChairman
} from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import LoadingContainer from "@/components/LoadingContainer.vue";

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
  document.getElementById('app').scrollIntoView({ behavior: 'smooth' })
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
    <div class="side-infos-list">
      <ApplicationConnectionLinks :connections-data="connectionsData" />
    </div>


    <div class="content-container split">

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

      <div v-if="!readonly" class="application-buttons-container">
        <ButtonLink @click="discardChanges">Änderungen verwerfen</ButtonLink>
        <ButtonLink @click="saveChanges">Speichern</ButtonLink>
      </div>

    </div>

    <div class="mid-right-fixed-container">
      <NotSavedIcon :display="unsaved"/>
      <ApplicationControl @click="collapseAll" type="collapse"/>
      <ApplicationControl @click="unCollapseAll" type="expand"/>
      <MoveTop @click="scrollTop"/>
    </div>

    <div v-if="!readonly">
      <ButtonLink v-if="passOnStatus === 'NOT_ALLOWED'" :disabled="true" :fixed="true" :redButton="true">Weitergeben
      </ButtonLink>
      <ButtonLink v-else-if="passOnStatus === 'PASSON'" :fixed="true" :redButton="true" @click="triggerPassOn">Weitergeben
      </ButtonLink>
      <ButtonLink v-else-if="passOnStatus === 'REJECT'" :fixed="true" :redButton="true" @click="triggerPassOn">
        Zurückweisen
      </ButtonLink>
    </div>
  </div>
  <div v-else class="main centered">
    <LoadingContainer />
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.side-infos-list {
  position: sticky;
  top: spacing(m);

  @include breakpoint(l) {
    display: none;
  }
}

.mid-right-fixed-container {
  @include verticalList(s);
  width: fit-content;

  display: flex;
  flex-direction: column;
  align-items: flex-end;

  position: fixed;
  bottom: calc(spacing(l) + spacing(xxxl));
  right: spacing(s);
}
</style>