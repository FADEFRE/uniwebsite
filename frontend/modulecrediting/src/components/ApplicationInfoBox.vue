<script setup>
import ArrowIcon from "@/assets/icons/ArrowIcon.vue";
import { ref } from "vue";

const showInformation = ref(false)
</script>

<template>
  <div class="application-info-container">
    <h2>Informationen</h2>
    <div class="explanation-collapsed-container">
      <p class="text-justify">
        Wählen Sie den Studiengang aus, für den Sie sich Leistungen anrechnen lassen wollen.
        Tragen sie dann die Details der Leistungen ein. Hierfür können sie beliebig viele Modulzuweisungen hinzufügen.
      </p>
      <Button @click="showInformation = !showInformation">
        Erklärung anzeigen
        <ArrowIcon :direction="showInformation ? 'up' : 'down'" color="red" />
      </Button>
    </div>
    <div v-if="showInformation" class="explanation-expanded-container">
      <div class="explanation-item">
        <h3 class="h4">Modulzuweisung</h3>
        <ul class="points">
          <li>Ein oder mehrere Fremdmodule die als Modul(e) der Universität Leipzig angerechnet werden sollen.</li>
          <li>Es sollen nur konkret zusammengehörige Module in eine Modulzuweisung geschrieben werden.</li>
          <li>Für den gesamten Antrag können sie mehrere Modulzuweisungen hinzufügen.</li>
          <!-- todo add example -->
          <li>!!! Beispiel hinzufügen !!!</li>
        </ul>
      </div>
      <div class="explanation-item">
        <h3 class="h4">Fremdmodul</h3>
        <ul class="points">
          <li>Ein konkretes Modul, das an einer anderen Universität belegt wurde.</li>
          <li>Hierfür müssen die gegebenen Felder ausgefüllt werden.</li>
          <li>Geben Sie das Punktesystem an (LP, ECTS oder ähnliches).</li>
          <li>Als Modulbeschreibung muss eine PDF-Datei mit der offiziellen Beschreibung des einzelnen Modul
            hochgeladen werden.</li>
        </ul>
      </div>
      <div class="explanation-item">
        <h3 class="h4">Module der Universität Leipzig</h3>
        <ul class="points">
          <li>Wählen sie ein oder mehrere Modul(e) aus, für die Sie ihre Leistungen anrechnen lassen wollen.</li>
          <li>Sollten sie kein passendes Modul finden, besteht auch die Möglichkeit, keines auszuwählen.</li>
        </ul>
      </div>
      <div class="explanation-item">
        <h3 class="h4">Kommentar</h3>
        <ul class="points">
          <li>Hier können weitere Informationen zu dieser Modulzuweisung mitgeteilt werden, dies ist optional.</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">

</style>