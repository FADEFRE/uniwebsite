<script setup>
import ArrowIcon from "@/assets/icons/ArrowIcon.vue";
import { ref } from "vue";

const showInformation = ref(false)
const showExample = ref(false)
</script>

<template>
  <div class="application-info-container">
    <h2>Informationen</h2>
    <div class="explanation-collapsed-container">
      <p class="text-justify">
        Wählen Sie den Studiengang aus, für den Sie sich Leistungen anrechnen lassen wollen.
        Tragen sie dann die Details der Leistungen ein. Hierfür können sie beliebig viele Modulzuweisungen hinzufügen.
      </p>
    </div>
    <Button @click="showInformation = !showInformation" class="info-box-button">
      Erklärung anzeigen
      <ArrowIcon :direction="showInformation ? 'up' : 'down'" color="red" />
    </Button>
    <div v-if="showInformation" class="explanation-expanded-container">
      <div class="explanation-item">
        <h3 class="h4">Modulzuweisung</h3>
        <ul class="points">
          <li>Ein oder mehrere Fremdmodule die als Modul(e) der Universität Leipzig angerechnet werden sollen.</li>
          <li>Es sollen nur konkret zusammengehörige Module in eine Modulzuweisung geschrieben werden.</li>
          <li>Für den gesamten Antrag können sie mehrere Modulzuweisungen hinzufügen.</li>
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
    <Button @click="showExample = !showExample" class="info-box-button">
      Beispiel anzeigen
      <ArrowIcon :direction="showExample ? 'up' : 'down'" color="red" />
    </Button>
    <div v-if="showExample" class="explanation-expanded-container">
      <div class="explanation-item">
        <h3 class="h4">
          Modulzuweisung: "Automaten und Berechenbarkeit
          <ArrowIcon direction="right" color="red" aria-label="wird angerechnet für" />
          Automaten und Sprachen, Berechenbarkeit"
        </h3>
      </div>
      <div class="explanation-item">
        <h4>Fremdmodul</h4>
        <ul class="points">
          <li>Modulname "Automaten und Berechenbarkeit"</li>
          <li>Universität "Halle"</li>
          <li>Punkte "10"</li>
          <li>Punktsystem "LP"</li>
        </ul>
      </div>
      <div class="explanation-item">
        <h4>Module der Universität Leipzig</h4>
        <ul class="points">
          <li>"Automaten und Sprachen"</li>
          <li>"Berechenbarkeit"</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.explanation-collapsed-container {
  @include verticalList(s);

}
.explanation-expanded-container {
  @include verticalList(s);
  // border-bottom: 2px solid $dark-gray;
  margin-bottom: spacing(s);
  padding: spacing(xs);
}
</style>