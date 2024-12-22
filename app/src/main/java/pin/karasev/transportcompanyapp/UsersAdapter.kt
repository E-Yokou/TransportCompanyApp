package pin.karasev.transportcompanyapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pin.karasev.transportcompanyapp.models.User
import android.widget.Toast

class UsersAdapter(var users: List<User>, val context: AdminActivity) : RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val login: TextView = view.findViewById(R.id.user_login)
        val email: TextView = view.findViewById(R.id.user_email)
        val role: TextView = view.findViewById(R.id.user_role)
        val btnMakeAdmin: Button = view.findViewById(R.id.btn_make_admin)
        val btnMakeUser: Button = view.findViewById(R.id.btn_make_user)
        val btnViewTickets: Button = view.findViewById(R.id.btn_view_tickets)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = users[position]
        holder.login.text = "Логин: ${user.login}"
        holder.email.text = "Email: ${user.email}"
        holder.role.text =  "Роль:  ${user.role}"

        holder.btnMakeAdmin.setOnClickListener {
            if (user.role != "admin") {
                context.updateUserRole(user.id!!, "admin")
            } else {
                Toast.makeText(context, "Пользователь уже является администратором", Toast.LENGTH_SHORT).show()
            }
        }

        holder.btnMakeUser.setOnClickListener {
            if (user.role != "user") {
                context.updateUserRole(user.id!!, "user")
            } else {
                Toast.makeText(context, "Пользователь уже является обычным пользователем", Toast.LENGTH_SHORT).show()
            }
        }

        // Добавляем обработчик нажатия на весь элемент пользователя
        holder.itemView.setOnClickListener {
            val intent = Intent(context, UserTripsActivity::class.java)
            intent.putExtra("USER_ID", user.id) // Передаем id пользователя
            context.startActivity(intent)
        }

        holder.btnViewTickets.setOnClickListener {
            context.showUserTickets(users[position].login)
        }
    }
}
