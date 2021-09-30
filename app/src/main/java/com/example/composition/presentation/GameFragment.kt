package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.*
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level

class GameFragment : Fragment() {

    private lateinit var level: Level
    private val viewModel:GameViewModel by lazy {
        ViewModelProvider(
            this,
            AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(GameViewModel::class.java)
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.startGame(level)
        setValues()
    }

    private fun setValues(){
        viewModel.question.observe(viewLifecycleOwner){
            binding.tvSum.text = it.sum.toString()
            binding.tvLeftNumber.text = it.visibleNumber.toString()
            binding.tvOption2.text = it.rightAnswer.toString()
        }
        viewModel.formattedTime.observe(viewLifecycleOwner){
            binding.tvTimer.text = it
        }
        viewModel.progressAnswers.observe(viewLifecycleOwner){
            binding.tvAnswersProgress.text = it
        }
        viewModel.minPercent.observe(viewLifecycleOwner){
            binding.progressBar.setProgress(it)
        }

        binding.tvOption2.setOnClickListener {
            val key = binding.tvOption2.text.toString().toInt()
            viewModel.chooseAnswer(key)
        }
    }

    private fun launchGameFinishedFragment(gameResult: GameResult){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newIntent(gameResult))
            .addToBackStack(null)
            .commit()
    }


    private fun parseArgs(){
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{

        const val NAME = "GameFragment"
        private const val KEY_LEVEL = "level"

        fun newIntent(level: Level):GameFragment{
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}