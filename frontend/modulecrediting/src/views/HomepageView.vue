<script setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import SideInfoApplicationProcess from '@/components/side-info/SideInfoApplicationProcess.vue';
import ButtonLink from '@/components/button/ButtonLink.vue';
import { getApplicationExists } from "@/requests/application-requests";

const router = useRouter()

const isInvalid = ref(false)
const id = ref('')

const openDetailView = () => {
  isInvalid.value = !id.value
  if (!isInvalid.value) {
    const formattedId = getFormattedId();
    getApplicationExists(formattedId)
        .then(data => {
          if (data === false) {
            isInvalid.value = true;
            return;
          }

          const routeData = router.resolve({ name: 'statusDetail', params: { id: formattedId } });
          window.open(routeData.href, '_top');
        })
  }

}

const goToSubmitApplication = () => {
  router.push({ name: 'submitApplication' })
}

const validateInput = () => {
  id.value = id.value.replace(/[^0-9]/g, '');
  id.value = id.value.replace(/(\d)(?=(\d{1,5})+$)/g, '$1-');
  id.value = id.value.slice(0, 11);
}

const getFormattedId = () => {
  return id.value.replace(/-/g, '');
}

</script>

<template>
  <div class="main">
    <h1 class="screen-reader-only">{{ $t('HomepageView.SRHeading') }}</h1>

    <div class="content-container split">

      <div class="homepage-container">
        <h2>{{ $t('HomepageView.Application.Heading') }}</h2>
        <p class="text-justify">{{ $t('HomepageView.Application.Text') }}</p>
        <ButtonLink @click="goToSubmitApplication">{{ $t('HomepageView.Application.SubmitButton') }}</ButtonLink>
      </div>

      <div class="homepage-container">
        <h2>{{ $t('HomepageView.Status.Heading') }}</h2>
        <p class="text-justify">{{ $t('HomepageView.Status.Text') }}</p>
        <div>
          <div class="input-button-container">
            <InputText
                v-model="id" placeholder="0-0-0-0-0-0" :aria-label="$t('HomepageView.Status.AriaProcessNumber')"
                :class="{ 'invalid': isInvalid }" class="status-input gray"
                @keydown.enter.prevent="openDetailView" @input.prevent="validateInput"
            />
            <ButtonLink @click="openDetailView">{{ $t('HomepageView.Status.ViewButton') }}</ButtonLink>
          </div>
          <small v-if="isInvalid" class="invalid-text">{{ $t('HomepageView.Status.NotExisting') }}</small>
        </div>
      </div>

    </div>

    <aside class="side-infos-list">
      <SideInfoApplicationProcess/>
    </aside>

  </div>
</template>


<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.status-input {
  border: none;
  background-color: $gray;

  &:focus::placeholder {
    color: transparent;
  }

  &.invalid {
    border: 2px solid $red;
  }
}

.homepage-container {
  @include basicContainer();
  @include verticalListItem($white);
  @include verticalList(l);
}

.input-button-container {
  width: 100%;
  display: flex;
  flex-direction: row;
  @include breakpoint(xs) {
    flex-direction: column;
  }
}

.p-inputtext {
  width: 100%;
  max-width: 250px;

  @include breakpoint(xs) {
    max-width: 100%;
  }

  text-align: center;
  font-family: 'Jost';
  font-weight: 500;
  letter-spacing: 0.3rem;
}
</style>
