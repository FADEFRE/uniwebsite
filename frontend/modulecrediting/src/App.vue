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
  console.log(isMenuOpen.value);
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
          <img src="@/assets/icons/BurgerIcon.svg" alt="Menu" />
        </Button>
        <div class="nav-menu-container" :class="{ 'open': isMenuOpen }">
          <TheLanguageSelection />
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
  display: none;
}

@media (max-width: 920px) {
  .burger-menu {
    display: block; // Show burger menu icon on small screens
    background-color: $white;
    display: flex;
    justify-content: center;
    align-items: center;

    margin-right: 0.625rem;
    &:hover {
      background-color: $gray;
    }
}

.nav-menu-container.open {
    display: block;
    transform: translateY(0); // Slide in
  }
  .nav-menu-container {
    display: none; // Hide regular menu on small screens
    position: absolute;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: $dark-gray;
    transition: transform 0.3s ease-in-out;
    z-index: 4;

    transform: translateY(100%);

    
  }

  
}
</style>