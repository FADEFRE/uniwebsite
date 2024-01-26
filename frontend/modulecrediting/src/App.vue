<script setup>
import TheNavigation from "@/components/TheNavigation.vue";
import TheLanguageSelection from "@/components/TheLanguageSelection.vue";
import { onMounted, ref } from "vue";

onMounted(() => {
  document.cookie = 'locale=DE'
})

const isMenuOpen = ref(false);

function toggleMenu() {
  isMenuOpen.value = !isMenuOpen.value;
}
</script>

<template>
  <div>
    <header class="header-background">
      <div class="header-container">
        <a href="/" class="logo-container">
          <img class="logo" src="./assets/Universität_Leipzig_logo.svg" alt="Logo der Universität Leipzig" />
        </a>
        
        <Button class="burger-menu" @click="toggleMenu">
          <img src="@/assets/icons/BurgerIcon.svg" alt="Menu Open"/>
        </Button>

        <Button class="close-menu" :class="{ 'open': isMenuOpen }" @click="toggleMenu">
            <img src="@/assets/icons/CloseMenu.svg" alt="Menu Close"/>
        </Button>

        <div class="nav-menu-container" :class="{ 'closed': !isMenuOpen }">
          <TheLanguageSelection/>
          <TheNavigation @linkClicked="toggleMenu" :isMenuOpen="isMenuOpen"/>
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

.header-background {
  background-color: $white;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  align-self: stretch;

  background-image: url(assets/icons/HeaderBackground.svg);
  background-repeat: no-repeat;
  background-size: contain;
  background-position: right;

  @media screen and (max-width: 920px) {
    background-image: none;
  }
}

.logo-container {
  background-color: $white;
  padding: 0;
  margin: 0;

  &:hover {
    background-color: $white;
  }
}

.logo {
  width: 27.42188rem;
  height: 11.25rem;

  @media only screen and (max-width: 1170px) {
    width: 21rem;
    height: 9rem;
  }

  @media only screen and (max-width: 920px) {
    width: 16rem;
    height: 7rem;
  }
}

.nav-menu-container {
  display: flex;
  flex-direction: column;
  padding: 0rem 1.875rem;
  gap: 0.6rem;
}

.burger-menu {
  background-color: $white;
  margin-right: 0.625rem;
  padding: 0.8rem 1rem;

  &:hover {
    background-color: $gray;
  }

  display: none;
}

.close-menu {
  position: fixed;
  top: 1rem;
  right: 1rem;
  z-index: 5;
  border: 2px solid $white;
  padding: 1.2rem 1.2rem;

  display: none;
}

@media (max-width: 920px) {
  .burger-menu {
    display: flex;
  }
  .close-menu.open {
    display: flex;
  }

  .nav-menu-container {
    width: 100vw;
    height: 100vh;
    background-color: $dark-gray;
    z-index: 4;

    position: fixed;
    top: 0;
    left: 0;
    
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 2rem;

    &.closed {
      display: none;
    }
  }
}
</style>