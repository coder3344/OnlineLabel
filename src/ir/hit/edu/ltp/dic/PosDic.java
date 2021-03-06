package ir.hit.edu.ltp.dic;

import gnu.trove.map.hash.THashMap;

import ir.hit.edu.ltp.util.FullCharConverter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * class of POS dictionary
 * 
 * @author dzl
 * 
 */
public class PosDic
{
	private THashMap<String, Vector<String>> word2Pos;

	public PosDic(THashMap<String, Vector<String>> word2Pos)
	{
		this.word2Pos = word2Pos;
	}

	public PosDic()
	{
		this.word2Pos = new THashMap<String, Vector<String>>();
	}

	public int size()
	{
		return word2Pos.size();
	}

	public Vector<String> getPos(String word)
	{
		if (word2Pos.containsKey(word))
			return word2Pos.get(word);
		else
			return null;
	}

	public boolean containsKey(String word)
	{
		return word2Pos.containsKey(word);
	}

	/**
	 * load POS dictioanry from a file
	 * the file format is word pos1 pos2 ...
	 * each word in a line
	 * 
	 * @param dicFile
	 * @throws IOException
	 */
	public void loadDic(String dicFile) throws IOException
	{
		InputStreamReader is = new InputStreamReader(new FileInputStream(dicFile), "UTF-8");
		BufferedReader br = new BufferedReader(is);

		String line;
		while ((line = br.readLine()) != null)
		{
			if (line.trim().equals(""))
				continue;

			String[] token = line.trim().split(" ");

			token[0] = FullCharConverter.half2Fullchange(token[0]);

			if (word2Pos.containsKey(token[0]))
			{
				Vector<String> tmpPos = word2Pos.get(token[0]);
				for (int i = 1; i < token.length; i++)
					if (!tmpPos.contains(token[i]))
						tmpPos.add(token[i]);
				word2Pos.put(token[0], tmpPos);
			}
			else
			{
				Vector<String> posVec = new Vector<String>();
				for (int i = 1; i < token.length; i++)
					posVec.add(token[i]);

				word2Pos.put(token[0], posVec);
			}

		}
	}

	public void updateDic(final String trainFile) throws IOException
	{
		InputStreamReader is = new InputStreamReader(new FileInputStream(trainFile), "UTF-8");
		BufferedReader br = new BufferedReader(is);
		String line;
		while ((line = br.readLine()) != null)
		{
			if (line.trim().equals(""))
				continue;

			line = line.trim().replaceAll("\\t{1,}", " ");
			line = line.replaceAll("\\s{2,}", " ");

			String[] token = line.split(" ");
			for (int i = 0; i < token.length; i++)
			{
				String[] pair = token[i].split("_");
				//				pair[0] = FullCharConverter.half2Fullchange(pair[0]);
				if (word2Pos.containsKey(pair[0]))
					if (!word2Pos.get(pair[0]).contains(pair[1]))
					{
						Vector<String> pos = word2Pos.get(pair[0]);
						pos.add(pair[1]);
						word2Pos.put(pair[0], pos);
						pos = null;
					}
			}
		}
	}
}
