<template>
  <div>
    <header class="header-background">
      <div class="header-container">
        <img src="./assets/Universität_Leipzig_logo.svg" alt="Logo der Universität Leipzig" class="logo" />
        <div class="router-container">
          <TheNavigation />
        </div>
      </div>
    </header>
    <router-view class="main-content" />
  </div>
</template>

<style lang="scss">
  @import './assets/mixins.scss';
  @import './assets/variables.scss';
  @import './assets/main.scss';

  .header-container {
  
  display: flex;
  justify-content: space-between;
  align-items: center;
  align-self: stretch;
  
  
  background-color: $white;
}


.logo {
  width: 27.42188rem;
  height: 11.25rem;
}


.router-button {
  display: inline-block;
}
</style>

<script setup>
import TheNavigation from "@/components/TheNavigation.vue";
</script>