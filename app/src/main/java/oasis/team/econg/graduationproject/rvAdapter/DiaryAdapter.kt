package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.graduationproject.DiaryListActivity
import oasis.team.econg.graduationproject.data.JournalsResponseDto
import oasis.team.econg.graduationproject.databinding.ItemDiaryBinding
import oasis.team.econg.graduationproject.dialog.CheckDeleteJournalFragment
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class DiaryAdapter(val context: Context?): RecyclerView.Adapter<DiaryAdapter.DiaryHolder>() {
    var listData = mutableListOf<JournalsResponseDto>()
    var diaryList = context as DiaryListActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryHolder {
        val binding = ItemDiaryBinding.inflate(LayoutInflater.from(context), parent, false)
        return DiaryHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(arrData: MutableList<JournalsResponseDto>?){
        listData = arrData as ArrayList<JournalsResponseDto>
    }

    inner class DiaryHolder(val binding: ItemDiaryBinding): RecyclerView.ViewHolder(binding.root){
        private val checkDeleteJournalFragment = CheckDeleteJournalFragment()

        fun setData(data: JournalsResponseDto){
            var bitmap: Bitmap? = null
            val thread = object: Thread(){
                override fun run() {
                    try{
                        var url = URL(data.picture)
                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                        conn.connect()
                        val inputStream = conn.inputStream
                        bitmap =  BitmapFactory.decodeStream(inputStream)
                    }catch(e: IOException){
                        e.printStackTrace()
                    }
                }
            }
            if(!data.picture.isNullOrEmpty())
                thread.start()
            else
                binding.imgDiary.visibility = View.GONE

            try{
                if(!data.picture.isNullOrEmpty()){
                    thread.join()
                    binding.imgDiary.setImageBitmap(bitmap)
                }
                binding.date.text = data.date
                binding.content.text = data.content
                setOnClickListener(data)
            }catch (e: InterruptedException){
                e.printStackTrace()
            }
        }

        fun setOnClickListener(data: JournalsResponseDto){
            binding.btnDeleteDiary.setOnClickListener {
                checkDeleteJournalFragment.setDialogListener(object: CheckDeleteJournalFragment.CheckDeleteListener{
                    override fun onDeleteClicked() {
                        proceedDelete(data)
                    }
                })
                checkDeleteJournalFragment.show(diaryList.supportFragmentManager, "checkDeleteJournalFragment")
            }
        }

        fun proceedDelete(data: JournalsResponseDto){
            RetrofitManager.instance.deleteJournals(MyApplication.prefs.token, data.id, completion = {
                    responseState, msg ->
                when(responseState){
                    RESPONSE_STATE.OKAY -> {
                        Log.d(TAG, "DiaryAdapter - DeleteJournals() SUCCESS: $msg")
                        val index = listData.indexOf(data)
                        listData.removeAt(index)
                        this@DiaryAdapter.notifyItemRemoved(index)
                    }
                    RESPONSE_STATE.FAIL -> {
                        Toast.makeText(context, "데이터 삭제 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "DiaryAdapter - DeleteJournals() FAIL: $msg")
                    }
                    else -> {
                        Toast.makeText(context, "데이터 삭제 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}