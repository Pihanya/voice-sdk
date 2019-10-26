<template>
  <v-app>
    <v-app-bar app>
      <v-toolbar-title class="headline text-uppercase">
        <span>BEPIS SDK  </span>
        <span class="font-weight-light">ADMIN PANEL</span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn
        text
        href=""
        target="_blank"
      >
        <span class="mr-2">EXIT</span>
      </v-btn>
    </v-app-bar>

    <v-content>
      <Admin/>
    </v-content>
  </v-app>
</template>

<script>
import Admin from './components/Admin';
export default {
  name: 'App',
  components: {
    Admin
  },
  data: () => ({
    //
  }),
};
</script>
