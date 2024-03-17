<script setup>
import { ref, onBeforeMount } from "vue";
import TheNavigation from "@/components/TheNavigation.vue";
import TheLanguageSelection from "@/components/TheLanguageSelection.vue";
import translate from '@/i18n/translate';
import { intervalMilliSeconds } from "@/config/refreshTokenInterval-config"
import { runInterval } from "@/router/login";

onBeforeMount(() => {
    const locale = translate.guessDefaultLocale()
    translate.setCurrentLocale(locale)
})

const isMenuOpen = ref(false);

function openMenu() {
  isMenuOpen.value = true;
}
function closeMenu() {
  isMenuOpen.value = false;
}
setInterval(runInterval, intervalMilliSeconds)
</script>

<template>
  <div>

    <div class="min-height">
    
    <header class="header-background">
      <div class="header-container">
        <a tabindex="-1" href="/" class="logo-container" aria-hidden="true">
          <img tabindex="-1" class="logo" src="@/assets/Universität_Leipzig_logo.svg" alt="" />
        </a>
        
        <Button class="burger-menu" @click="openMenu">
          <img src="@/assets/icons/BurgerIcon.svg" :alt="$t('App.MenuOpen')"/>
        </Button>

        <Button class="close-menu" :class="{ 'open': isMenuOpen }" @click="closeMenu">
            <img src="@/assets/icons/CloseMenu.svg" :alt="$t('App.MenuClosed')"/>
        </Button>

        <div class="nav-menu-container" :class="{ 'closed': !isMenuOpen }">
          <TheLanguageSelection/>
          <TheNavigation @linkClicked="closeMenu" :isMenuOpen="isMenuOpen"/>
        </div>
      </div>
    </header>

    <router-view class="main-content" />

  </div>

    <footer>
      <div class="footer-content">
        <p>{{$t('App.LegalDisclosure')}}</p>
        <p>{{$t('App.PrivacyPolicy')}}</p>
      </div>
    </footer>
  </div>
</template>

<style lang="scss">
@use '@/assets/styles/global' as *;
@use '@/assets/styles/util' as *;
@use '@/assets/styles/components' as *;

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

  @include breakpoint(m) {
    background-image: none;
  }
}

.logo-container {
  background-color: $white;
  padding: 0;
  margin: 0;

  &:hover, &:focus {
    background-color: $white;
  }
}

.logo {
  width: 27rem;
  height: 11rem;

  @include breakpoint(l) {
    width: 16rem;
    height: 7rem;
  }
}

.nav-menu-container {
  display: flex;
  flex-direction: column;
  padding: spacing(xl);
  gap: spacing(s);
}

.burger-menu {
  background-color: $white;
  margin-right: spacing(m);
  padding: spacing(m);

  &:hover, &:focus {
    background-color: $gray;
  }

  display: none;
}

.close-menu {
  position: fixed;
  top: 1.8rem;
  right: spacing(m);
  z-index: 5;
  border: 2px solid $white;
  padding: 1.2rem;

  display: none;
}

@include breakpoint(m) {
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
    gap: spacing(xl);

    &.closed {
      display: none;
    }
  }
}

.min-height {
  min-height: 100vh;
}

footer {
  width: 100%;
  background-color: $white;

  padding: spacing(m);

  display: flex;
  justify-content: center;
  align-items: flex-end;
}

.footer-content {
  display: flex;
  gap: spacing(m);

}
</style>