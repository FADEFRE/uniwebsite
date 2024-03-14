<script setup>
import { useUserStore } from "@/store/userStore";
import translate from '@/i18n/translate';
import { useRoute } from "vue-router";
import { ref, watch } from "vue";

const store = useUserStore()
const showLanguage = ref()

watch(useRoute(), newRoute => {
  showLanguage.value = newRoute.meta['authType'] === 'standard'
})
</script>

<template>
  <div v-if="showLanguage" class="language-selection-container">
    <Button
        :aria-label="$t('TheLanguageSelection.AriaChooseGerman')"
        @click="translate.switchLanguage('de')"
        class="language-button"
        :class="{ 'active': store.locale == 'de' }"
    >
      DE
    </Button>
    <Button
        :aria-label="$t('TheLanguageSelection.AriaChooseEnglish')"
        @click="translate.switchLanguage('en')"
        class="language-button"
        :class="{ 'active': store.locale == 'en' }"
    >
      EN
    </Button>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.language-selection-container {
  width: fit-content;
  display: flex;
}

.language-button {
  background-color: transparent;
  padding: 0 spacing(s);

  color: $white;
  font-family: "Jost";
  font-size: 1rem;
  font-weight: 400;
  line-height: spacing(m);

  &:first-child {
    border-right: 1px solid $white;
  }

  &:hover,
  &:focus {
    background-color: transparent;
    text-decoration: underline;
  }
}


.active {
  font-weight: 550;
}</style>