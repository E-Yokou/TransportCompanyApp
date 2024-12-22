package pin.karasev.transportcompanyapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pin.karasev.transportcompanyapp.models.TicketDto

class TodayTicketsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tickets_list, container, false)
        val ticketsList: RecyclerView = view.findViewById(R.id.ticketsList)

        val userLogin = requireArguments().getString("USER_LOGIN") ?: ""
        val db = DbHelper(requireContext(), null)
        val userId = db.getCurrentUserId(userLogin)
        val todayTickets = db.getUserTicketsForToday(userId)

        ticketsList.layoutManager = LinearLayoutManager(requireContext())
        ticketsList.adapter = TicketsAdapter(todayTickets)

        return view
    }
}