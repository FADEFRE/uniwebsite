<template>
  <div>
    <header class="header-background">
      <div class="header-container">
        <img src="./assets/Universität_Leipzig_logo.svg" alt="Logo der Universität Leipzig" class="logo" />
        <div class="router-container">
          <router-link to="/submit" class="router-button">Antrag</router-link>
          <router-link to="/status" class="router-button">Status</router-link>
        </div>
      </div>
    </header>
    <router-view class="main-content" />
  </div>
</template>

<style lang="scss">
  @import './assets/main.scss';

  .header-container {
  
  display: flex;
  justify-content: space-between;
  align-items: center;
  align-self: stretch;
  
  
  background-color: $white;
}


.logo {
  height: 150px;
}


.router-button {
  display: inline-block;
}
</style>

