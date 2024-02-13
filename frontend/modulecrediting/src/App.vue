<script setup>
import TheNavigation from "@/components/TheNavigation.vue";
import TheLanguageSelection from "@/components/TheLanguageSelection.vue";
import { onBeforeMount, ref } from "vue";
import translate from '@/i18n/translate';

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
</script>

<template>
  <div>
    <header class="header-background">
      <div class="header-container">
        <a href="/" class="logo-container">
          <img class="logo" src="./assets/Universität_Leipzig_logo.svg" alt="Logo der Universität Leipzig" />
        </a>
        
        <Button class="burger-menu" @click="openMenu">
          <img src="@/assets/icons/BurgerIcon.svg" alt="Menu Open"/>
        </Button>

        <Button class="close-menu" :class="{ 'open': isMenuOpen }" @click="closeMenu">
            <img src="@/assets/icons/CloseMenu.svg" alt="Menu Close"/>
        </Button>

        <div class="nav-menu-container" :class="{ 'closed': !isMenuOpen }">
          <TheLanguageSelection/>
          <TheNavigation @linkClicked="closeMenu" :isMenuOpen="isMenuOpen"/>
        </div>
      </div>
    </header>

    <router-view class="main-content" />

    <footer>
      <ul>
        <li><p>Impressum</p></li>
        <li><p>Datenschutz</p></li>
      </ul>
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

  &:hover {
    background-color: $white;
  }
}

.logo {
  width: 27rem;
  height: 11rem;

  @include breakpoint(l) {
    width: 21rem;
    height: 9rem;
  }

  @include breakpoint(m) {
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

  &:hover {
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


footer {
  background-image: url('@/assets/icons/footer.svg');
  background-size: cover;
  width: 100%;
  min-height: 700px;

  padding: spacing(m);

  display: flex;
  justify-content: center;
  align-items: flex-end;
}

ul {
  display: flex;
  gap: spacing(m);

  li p 
  {
    color: $white;
  }
}
</style>