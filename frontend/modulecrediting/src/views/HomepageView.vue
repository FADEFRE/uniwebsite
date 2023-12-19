<script setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { url } from "@/scripts/url-config";
import axios from "axios";
import HomepageContainer from '../components/HomepageContainer.vue';
import SideInfoContainer from '../components/SideInfoContainer.vue';

const router = useRouter()

const isInvalid = ref(false)
const id = ref('')

const openDetailView = () => {
    axios.get(url + `/applications/${id.value}/exists`)
        .then(response => {
            if (response.data) {
                const routeData = router.resolve({ name: 'statusDetail', params: { id: id.value } })
                window.open(routeData.href, '_top')
            } else {
                isInvalid.value = true
            }
        })
}

const goToSubmitApplication = () => {
    router.push({ name: 'SubmitApplication' })
}
</script>

<template>
    <div class="homepage-container">
        <div class="main-content">
            <!-- HomepageContainer Application -->
            <HomepageContainer :header="'Antrag stellen'"
                :text="'Erstellen Sie einen Antrag, um Module anderer Universitäten an der Universität Leipzig anzurechnen lassen.'"
                class="panel wide">
                <Button @click="goToSubmitApplication" class="p-button-success">einen Antrag erstellen</Button>
            </HomepageContainer>

            <!-- HomepageContainer StatusView -->
            <HomepageContainer :header="'Status einsehen'"
                :text="'Sehen Sie den Status von bereits gestellten Anträgen über die 6-stellige Vorgangsnummer ein.'"
                class="panel wide">
                <InputText v-model="id" :class="{ 'p-invalid': isInvalid }" placeholder="z.B. 133769" />
                <Button @click="openDetailView" class="p-button-primary">Status einsehen</Button>
            </HomepageContainer>

            <!--SideInfoContainerfür Antragprozess -->
            <SideInfoContainer :heading="'Antragsprozess'">
                <ul>
                    <li>Antrag online stellen</li>
                    <li>Über Vorgangsnummer online Status einsehen</li>
                    <li>Auf Entscheidung des PAV warten</li>
                    <li>Mit abgeschlossenem Antrag zum Studienbüro gehen</li>
                </ul>
            </SideInfoContainer>
        </div>
    </div>
</template>

<style scoped>
.homepage-container {
    display: flex;
    justify-content: center;
}

.main-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
    margin-top: 50px;
}



.panel,
.sidebar {
    border: 2px solid #1e1717;
    padding: 10px;
    width: 100%;
    max-width: 400px;
}

.wide {
    width: 100%;
    max-width: unset;
}

.narrower {
    width: 50%;
}
</style>
