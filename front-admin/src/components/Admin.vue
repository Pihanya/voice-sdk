<template>
  <v-card>
    <v-toolbar
            color="primary"
            dark
            flat
    >
      <v-icon>build</v-icon>
      <v-toolbar-title class="pl-3">COMMANDS</v-toolbar-title>
    </v-toolbar>

    <v-row
            class="pa-4"
            justify="space-between"
    >
      <v-col cols="5">
        <v-treeview
                :active.sync="active"
                :items="commands"
                :open.sync="open"
                activatable
                color="warning"
                open-on-click
                transition
        >
            <v-btn text icon color="red">
                <v-icon>close</v-icon>
            </v-btn>
          <template v-slot:prepend="{ item, active }">
            <v-icon v-if="!item.children">touch_app</v-icon>
          </template>
        </v-treeview>
              <v-dialog
                      v-model="dialog"
                      width="500"
              >
                  <template v-slot:activator="{ on }">
                      <v-btn class="ma-2" tile outlined color="success"  v-on="on">
                          <v-icon left>control_point</v-icon> Add new Command
                      </v-btn>
                      <v-btn class="mx-2" fab dark small color="red">
                          <v-icon dark>delete</v-icon>
                      </v-btn>
                  </template>

                  <v-card>
                      <v-card-title
                              class="headline grey lighten-2"
                              primary-title
                      >
                          Add new command
                      </v-card-title>

                      <v-card-text>
                          <v-text-field
                                  label="New command"
                                  v-model="formCommand"
                          ></v-text-field>
                          <v-text-field
                                  label="Words"
                                  v-model="formWords"
                          ></v-text-field>
                      </v-card-text>

                      <v-divider></v-divider>

                      <v-card-actions>
                          <v-spacer></v-spacer>
                          <v-btn
                                  color="primary"
                                  text
                                  @click="postCommand"
                          >
                              <v-icon left>add</v-icon> add
                          </v-btn>
                      </v-card-actions>
                  </v-card>
              </v-dialog>
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
          >
              <v-text-field
                      v-model="message"
                      label="Add new word"
                      clearable
                      :append-icon="send"
                      @click:append="post"
                      v-on:keyup.enter="post"
                      @click:clear="clearMessage"
              ></v-text-field>
              <template
                         v-for="(kek ,index) in selected.aliases"
                         flat>
                  <div
                          class="d-flex flex-wrap-reverse"
                          :key="index+kek"
                  >
                          <v-btn text class="pa-2" icon :key="index+kek" color="red" >
                              <v-icon>close</v-icon>
                          </v-btn>
                          <div class="pa-2 pb-1" :key="index+kek">
                              {{ kek }}
                          </div>
                  </div>

                  <v-divider  :key="index+kek"></v-divider>
              </template >
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
          message: '',
          marker: true,
          dialog: false,
          formCommand: '',
          formWords: ''
      }),
      computed: {
          items() {
              return [
                  {
                      name: 'Command',
                      children: this.users,
                  },
              ]
          },
          commands() {
              return this.$store.getters.command
          },
          selected() {
              this.clearMessage()
            //  if (!this.active.length) return undefined
              const id = this.active[0]
              return this.commands.find(command => command.id === id)
          }
      },
      created() {
          this.$store.dispatch('loadCommand').then(() => {
          })
      },
      methods:{
          post (){
              const postjson = {
                  name: this.selected.name,
                  aliases: this.message.split(',')
              }
              this.$store.dispatch('postWord', JSON.stringify(postjson)).then(() => {})
          },
          clearMessage () {
              this.message = ''
          },
          postCommand (){
              this.dialog = false
              const postjson = {
                  name: this.formCommand,
                  aliases: this.formWords.split(',')
              }
              this.$store.dispatch('postWord', JSON.stringify(postjson)).then(() => {
                  this.formCommand = '';
                  this.formWords = '';
              })
          }
      }
  }
</script>

