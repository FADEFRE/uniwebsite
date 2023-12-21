<template>
  <div>
    <header class="header-background">
      <div class="header-container">
        <img src="./assets/Universität_Leipzig_logo.svg" alt="Logo der Universität Leipzig" class="logo" />
        <div class="spacer"></div>
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

.view-container {
  margin: 10px 20px 0;
}

</style>

<style scoped>
.header-container {
  height: 7vh;
  display: flex;
  align-items: center;
  border-width: 1px;
  border-color: #aaaaaa;
  border-bottom-style: solid;
  background-color: white;
}


.header-background {
  background-image: none;

}


.logo {
  height: 150%;
  width: auto;
}

.spacer {
  flex: 1;
}

.router-button {
  display: inline-block;
  padding: 10px 20px;
  background-color: #b02f2c;
  color: #fff;
  text-decoration: none;
  border-radius: 5px;
  transition: background-color 0.3s ease;
  margin-left: 15px;
}
</style>
