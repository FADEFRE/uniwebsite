<script setup>

import ModuleStatusIcon from "@/assets/icons/ModuleStatusIcon.vue";
</script>

<template>
  <div class="completed-info-container">
    <h2>Wie geht es weiter?</h2>
    <p>
      Es wurde eine finale Entscheidung zu ihrem Antrag getroffen.
      Dies ist nur eine Informationen über die Möglichkeit der Anrechnung.
      Um sich ihre Leistungen offiziell anrechnen zu lassen, müssen sie zum Studienbüro gehen.
      Bringen sie hierfür bitte alle relevanten Dokumente mit, die den Abschluss der anzurechnenden Leistungen belegen.
      Falls sie weitere Fragen zu ihrem Antrag haben, kontaktieren sie bitte das Studienbüro.
    </p>
    <h3>Legende</h3>
    <ul>
      <li>
        <ModuleStatusIcon status-decision="accepted" />
        <p>Anrechnung angenommen</p>
      </li>
      <li>
        <ModuleStatusIcon status-decision="asExamCertificate" />
        <p>Anrechnung als Übungsschein</p>
      </li>
      <li>
        <ModuleStatusIcon status-decision="denied" />
        <p>Anrechnung abgelehnt</p>
      </li>
    </ul>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.completed-info-container {
  @include verticalListItem($white);
  @include basicContainer();

  margin-bottom: spacing(m);
}
</style>