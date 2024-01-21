<script setup>
import LoginContainer from '../components/LoginContainer.vue';
import PanelHeader from '../components/PanelHeader.vue';
import PanelStatusIcons from '../components/PanelStatusIcons.vue';
import PanelExternalModules from '../components/PanelExternalModules.vue';
import ApplicationOverview from '../components/ApplicationOverview.vue';
import PanelInternalModules from '../components/PanelInternalModules.vue';
import SideInfoContainer from '../components/SideInfoContainer.vue';

const externalModules = ['Analysis I']
const internalModules = ['Analysis für Informatiker'];
</script>

<template>
  
  <div class="main">
    <div class="side-infos-container">
            <!--SideInfoContainerfür Antragprozess -->
            <SideInfoContainer :heading="'ANTRAGSPROZESS'">
              <ul>
                <li>Antrag online stellen</li>
                <li>Über Vorgangsnummer online Status einsehen</li>
                <li>Auf Entscheidung des PAV warten</li>
                <li>Mit abgeschlossenem Antrag zum Studienbüro gehen</li>
              </ul>
            </SideInfoContainer>
            <SideInfoContainer :heading="'STUDIENBÜRO'">
              <p>Fakultät für Mathematik und Informatik</p>
              <div class="main-info-container">
                <div class="info-group-container">
                  <h4>Anschrift</h4>
                  <ul>
                    <li>Neues Augusteum</li>
                    <li>Augustusplatz 10</li>
                    <li>04109 Leipzig</li>
                  </ul>
                </div>
                <div class="info-group-container">
                  <h4>Kontakt</h4>
                  <ul>
                    <li>Telefon: +49 341 97-32165</li>
                    <li>Telefax: +49 341 97-32193</li>
                    <li>E-Mail: studienbuero@math.uni-leipzig.de</li>
                  </ul>
                </div>
                <div class="info-group-container">
                  <h4>Sprechzeiten</h4>
                  <p>Dienstag und Donnerstag: 9:00 - 11:30 Uhr und 12:30 - 16:00 Uhr</p>
                </div>
                <ButtonLink>Zum Studienbüro</ButtonLink>
              </div>

            </SideInfoContainer>
          </div>
    <div class="panel-header">
      <div class="ellipsis">Algorithmen und dsddasdasdasd 1ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd</div>
    </div>
  </div>
</template>


<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';


.main {
  @include main();
}

.side-infos-container {
  @include verticalList(big);
  width: min-content;

  @media only screen and (max-width: 1000px) {
    width: 100%;
  }
}

.panel-header {
  width: 1300px;
  background-color: $dark-gray;
  overflow: hidden;
}
.ellipsis {
  color: $white;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
</style>
