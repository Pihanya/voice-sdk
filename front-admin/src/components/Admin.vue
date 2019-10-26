<template>
  <v-card>
    <v-toolbar
            color="primary"
            dark
            flat
    >
      <v-icon>adb</v-icon>
      <v-toolbar-title>COMMAND</v-toolbar-title>
    </v-toolbar>

    <v-row
            class="pa-4"
            justify="space-between"
    >
      <v-col cols="5">
        <v-treeview
                :active.sync="active"
                :items="items"
                :load-children="fetchUsers"
                :open.sync="open"
                activatable
                color="warning"
                open-on-click
                transition
        >
          <template v-slot:prepend="{ item, active }">
            <v-icon v-if="!item.children">accessible</v-icon>
          </template>
        </v-treeview>
      </v-col>

      <v-divider vertical></v-divider>

      <v-col
              class="d-flex"
      >
        <v-scroll-y-transition mode="out-in">
          <div
                  v-if="!selected"
                  class="title grey--text text--lighten-1 font-weight-light"
                  style="align-self: center;"
          >
            Select a Command
          </div>
          <v-list
                  v-else
                  :key="selected.id"
                  flat
          >
            <v-list-text>
              <div>
                {{ selected.title }}
              </div>
            </v-list-text>
            <v-divider></v-divider>
          </v-list>
        </v-scroll-y-transition>
      </v-col>
    </v-row>
  </v-card>
</template>

<script>

  export default {
    data: () => ({
      active: [],
      open: [],
      users: [],
    }),

    computed: {
      items () {
        return [
          {
            name: 'Command',
            children: this.users,
          },
        ]
      },
      selected () {
        if (!this.active.length) return undefined

        const id = this.active[0]

        return this.users.find(user => user.id === id)
      },
    },
    methods: {
      fetchUsers (item) {
        return fetch('https://my-json-server.typicode.com/typicode/demo/posts')
                .then(res => res.json())
                .then(json => (item.children.push(...json)))
      }
    },
  }
</script>
